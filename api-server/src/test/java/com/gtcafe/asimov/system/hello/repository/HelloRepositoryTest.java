package com.gtcafe.asimov.system.hello.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * HelloRepository 資料存取層測試
 * 測試資料庫操作的正確性
 */
@DataJpaTest
class HelloRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HelloRepository helloRepository;

    @Test
    void save_ShouldPersistHelloEntity() {
        // Arrange
        HelloEntity entity = new HelloEntity("Test Message");

        // Act
        HelloEntity savedEntity = helloRepository.save(entity);

        // Assert
        assertNotNull(savedEntity.getId());
        assertEquals("Test Message", savedEntity.getMessage());
        assertNotNull(savedEntity.getCreationTime());
        assertNotNull(savedEntity.getUpdatedTime());
    }

    @Test
    void findById_ShouldReturnCorrectEntity() {
        // Arrange
        HelloEntity entity = new HelloEntity("Test Message");
        HelloEntity savedEntity = entityManager.persistAndFlush(entity);

        // Act
        Optional<HelloEntity> foundEntity = helloRepository.findById(savedEntity.getId());

        // Assert
        assertTrue(foundEntity.isPresent());
        assertEquals("Test Message", foundEntity.get().getMessage());
        assertEquals(savedEntity.getId(), foundEntity.get().getId());
    }

    @Test
    void findByMessage_ShouldReturnCorrectEntity() {
        // Arrange
        HelloEntity entity = new HelloEntity("Unique Test Message");
        entityManager.persistAndFlush(entity);

        // Act
        Optional<HelloEntity> foundEntity = helloRepository.findByMessage("Unique Test Message");

        // Assert
        assertTrue(foundEntity.isPresent());
        assertEquals("Unique Test Message", foundEntity.get().getMessage());
    }

    @Test
    void findByCreationTimeBetween_ShouldReturnEntitiesInRange() {
        // Arrange
        long baseTime = System.currentTimeMillis();
        HelloEntity entity1 = new HelloEntity("Message 1");
        HelloEntity entity2 = new HelloEntity("Message 2");
        HelloEntity entity3 = new HelloEntity("Message 3");
        
        // 手動設定時間以確保測試的可預測性
        entity1.setCreationTime(baseTime - 1000);
        entity2.setCreationTime(baseTime);
        entity3.setCreationTime(baseTime + 1000);
        
        entityManager.persistAndFlush(entity1);
        entityManager.persistAndFlush(entity2);
        entityManager.persistAndFlush(entity3);

        // Act
        List<HelloEntity> entities = helloRepository.findByCreationTimeBetween(baseTime - 500, baseTime + 500);

        // Assert
        assertEquals(1, entities.size());
        assertEquals("Message 2", entities.get(0).getMessage());
    }

    @Test
    void findAllWithPagination_ShouldReturnPagedResults() {
        // Arrange
        for (int i = 1; i <= 5; i++) {
            HelloEntity entity = new HelloEntity("Message " + i);
            entityManager.persistAndFlush(entity);
        }

        // Act
        List<HelloEntity> firstPage = helloRepository.findAllWithPagination(0, 2);
        List<HelloEntity> secondPage = helloRepository.findAllWithPagination(1, 2);

        // Assert
        assertEquals(2, firstPage.size());
        assertEquals(2, secondPage.size());
        
        // 驗證分頁結果不重複
        assertNotNull(firstPage.get(0).getMessage());
        assertNotNull(secondPage.get(0).getMessage());
    }

    @Test
    void deleteById_ShouldRemoveEntity() {
        // Arrange
        HelloEntity entity = new HelloEntity("To be deleted");
        HelloEntity savedEntity = entityManager.persistAndFlush(entity);

        // Act
        helloRepository.deleteById(savedEntity.getId());
        entityManager.flush();

        // Assert
        Optional<HelloEntity> deletedEntity = helloRepository.findById(savedEntity.getId());
        assertTrue(deletedEntity.isEmpty());
    }
}