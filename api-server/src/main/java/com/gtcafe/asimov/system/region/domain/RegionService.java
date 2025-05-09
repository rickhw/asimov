package com.gtcafe.asimov.system.region.domain;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.system.region.RegionMapper;
import com.gtcafe.asimov.system.region.repository.RegionEntity;
import com.gtcafe.asimov.system.region.repository.RegionRepository;
import com.gtcafe.asimov.system.region.schema.Region;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionService {

    // inject dependencies
    private final CacheRepository cacheRepos;
    private final RegionRepository dbRepos;
    private final JsonUtils jsonUtils;
    
    // cache key prefix
    private static final String CACHE_KEY_PREFIX = "region:";

    // for query/read cache
    private static final Duration CACHE_TTL = Duration.ofMinutes(3);
    private static final Duration LOCK_TIMEOUT = Duration.ofSeconds(10);
    
    // for flush cache
    private static final String FLUSH_LOCK_KEY = "region:flush:lock";
    private static final Duration FLUSH_LOCK_TIMEOUT = Duration.ofMinutes(5);
    private static final int BATCH_SIZE = 20;

    /**
     * 將資料庫中的所有 Region 資料同步到快取
     * 使用批次處理來提高效能
     */
    @Transactional(readOnly = true)
    @Retryable(value = DataAccessException.class, maxAttempts = 3)
    public void flush() {
        boolean lockAcquired = false;
        
        try {
            // 1. 嘗試獲取全局刷新鎖
            lockAcquired = cacheRepos.setIfNotExists(FLUSH_LOCK_KEY, getLockString(), FLUSH_LOCK_TIMEOUT);
            if (!lockAcquired) {
                log.warn("Another flush operation is in progress");
                // throw new ConcurrentOperationException("Another flush operation is in progress");
            }

            log.info("Starting region cache flush operation");
            
            // 2. 從資料庫獲取所有資料（使用分頁避免內存溢出）
            int pageNumber = 0;
            List<RegionEntity> batch;
            Set<String> processedKeys = new HashSet<>();
            
            do {
                log.info("1. find all with pagination, pageNumber: {}, batchSize: {}", pageNumber, BATCH_SIZE);
                batch = dbRepos.findAllWithPagination(pageNumber, BATCH_SIZE);
                if (batch.isEmpty()) {
                    break;
                }

                // 3. 處理每一批次資料
                log.info("2. processBatch, pageNumber: {}, batchSize: {}", pageNumber, batch.size());
                processBatch(batch, processedKeys);
                pageNumber++;
                
                log.debug("Processed batch {}, size: {}", pageNumber, batch.size());
            } while (!batch.isEmpty());

            // 4. 清理過期的快取項（清除那些在資料庫中已不存在的項）
            log.info("3. cleanupStaleCache");
            cleanupStaleCache(processedKeys);
            
            log.info("Region cache flush completed successfully. Total processed: {}", processedKeys.size());
            
        } catch (Exception e) {
            log.error("Error during region cache flush operation", e);
            throw new RuntimeException("Failed to flush region cache", e);
        } finally {
            if (lockAcquired) {
                cacheRepos.delete(FLUSH_LOCK_KEY);
                log.debug("Released flush lock");
            }
        }
    }

    private void processBatch(List<RegionEntity> entities, Set<String> processedKeys) {
        for (RegionEntity entity : entities) {
            try {
                Region region = RegionMapper.mapEntityToDomain(entity);
                updateCache(region);
                processedKeys.add(generateCacheKey(region.getRegionCode()));
            } catch (Exception e) {
                log.error("Error processing region entity: {}", entity, e);
                // 繼續處理下一個，不中斷整個批次
            }
        }
    }

    private void cleanupStaleCache(Set<String> validKeys) {
        try {
            Set<String> existingKeys = cacheRepos.findKeysByPattern(CACHE_KEY_PREFIX + "*");
            existingKeys.removeAll(validKeys);

            if (!existingKeys.isEmpty()) {
                log.info("Removing {} stale cache entries", existingKeys.size());
                for (String key : existingKeys) {
                    cacheRepos.delete(key);
                }
            }
        } catch (Exception e) {
            log.warn("Error during cache cleanup", e);
            // 清理過期快取失敗不應影響主要的刷新操作
        }
    }


    @Transactional
    public Region create(Region region) {
        try {
            RegionEntity entity = RegionMapper.mapDomainToEntity(region);
            dbRepos.save(entity);

            region.setResourceId(entity.getId());
            
            updateCache(region);
            
            return region;
        } catch (Exception e) {
            log.error("Error creating region: [{}]", region, e);
            throw new RuntimeException("Failed to create region", e);
        }
    }

    // Cache-aside pattern
    @Retryable(value = DataAccessException.class, maxAttempts = 3)
    public Region retrieve(String regionCode) {
        String cacheKey = generateCacheKey(regionCode);
        
        // 1. Try cache first
        Optional<Region> cachedRegion = getFromCache(cacheKey);
        if (cachedRegion.isPresent()) {
            log.info("Cache hit for region: [{}]", regionCode);
            return cachedRegion.get();
        }

        // 1.1 Cache miss - try to acquire lock
        String lockKey = cacheKey + ":lock";
        boolean lockAcquired = false;
        
        try {
            lockAcquired = cacheRepos.setIfNotExists(lockKey, getLockString(), LOCK_TIMEOUT);
            log.debug("Lock acquired: [{}]", lockAcquired);
            
            if (lockAcquired) {
                // Double check cache after acquiring lock
                cachedRegion = getFromCache(cacheKey);
                if (cachedRegion.isPresent()) {
                    return cachedRegion.get();
                }

                // Load from database
                Region region = loadFromDatabase(regionCode);
                if (region != null) {
                    updateCache(region);
                    log.info("Cache miss for region: [{}]", regionCode);
                    return region;
                }
                
                return null; // @TODO
                // throw new ResourceNotFoundException("Region not found: " + regionCode);
            } else {
                // Wait for other thread to load data
                Thread.sleep(100);
                return retrieve(regionCode);
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Operation interrupted", e);
        } finally {
            if (lockAcquired) {
                cacheRepos.delete(lockKey);
            }
        }
    }

    private Optional<Region> getFromCache(String cacheKey) {
        log.debug("Reading from cache for cacheKey: [{}]", cacheKey);
        try {
            String cachedValue = cacheRepos.retrieveObject(cacheKey)
                .orElse(null);
                
            if (cachedValue == null) {
                return Optional.empty();
            }
            
            return jsonUtils.jsonStringToModelSafe(cachedValue, Region.class);
        } catch (Exception e) {
            log.warn("Error reading from cache for cacheKey: [{}]", cacheKey, e);
            return Optional.empty();
        }
    }

    private Region loadFromDatabase(String regionCode) {
        log.debug("Loading region from database, regionCode: [{}]", regionCode);
        RegionEntity entity = dbRepos.findByRegionCode(regionCode);
        return entity != null ? RegionMapper.mapEntityToDomain(entity) : null;
    }

    private void updateCache(Region region) {
        log.debug("Updating cache for region: [{}]", region.getRegionCode());
        try {
            String cacheKey = generateCacheKey(region.getRegionCode());
            String jsonString = jsonUtils.modelToJsonString(region);
            cacheRepos.saveOrUpdateObject(cacheKey, jsonString, CACHE_TTL.toSeconds(), TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error updating cache for region: [{}]", region.getRegionCode(), e);
        }
    }

    private String generateCacheKey(String regionCode) {
        return CACHE_KEY_PREFIX + regionCode;
    }

    private String getLockString() {
        return Long.toString(Thread.currentThread().getId());
    }
}