package com.gtcafe.asimov.apiserver.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;


@Service
public class CacheObjectRepository {

  // @Autowired
  private final RedisTemplate<String, Object> _redisTemplate;
  private final ValueOperations<String, Object> _valueOps;

  @Autowired
  public CacheObjectRepository(RedisTemplate<String, Object> redisTemplate) {
    this._redisTemplate = redisTemplate;
    this._valueOps = redisTemplate.opsForValue();
  }

  // Create or Update any type of DomainObject
  public <T> void saveOrUpdateObject(String key, T object) {
    this._valueOps.set(key, object);

    // Optional: Set expiration time if needed
    // valueOps.set(key, domainObject, 1, TimeUnit.HOURS);
  }

  @SuppressWarnings("unchecked")
  public <T> T retrieveObject(String key, Class<T> clazz) {
      Object result = _valueOps.get(key);
      if (result != null && clazz.isInstance(result)) {
          return (T) result;
      }
      return null;
  }


  // Update any type of DomainObject
  public <T> void updateDomainObject(String key, T object) {
    if (Boolean.TRUE.equals(_redisTemplate.hasKey(key))) {
        _valueOps.set(key, object);
    } else {
        throw new RuntimeException("DomainObject not found for key: " + key);
    }
  }

  // Delete a DomainObject by key
  public void deleteDomainObject(String key) {
      _redisTemplate.delete(key);
  }

}
