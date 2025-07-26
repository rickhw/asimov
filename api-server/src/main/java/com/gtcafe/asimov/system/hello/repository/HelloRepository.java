package com.gtcafe.asimov.system.hello.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Hello 資料庫存取介面
 * 提供 Hello 相關的資料庫操作
 */
@Repository
public interface HelloRepository extends JpaRepository<HelloEntity, String> {

    /**
     * 根據訊息內容查詢 Hello 實體
     * @param message 訊息內容
     * @return Hello 實體
     */
    Optional<HelloEntity> findByMessage(String message);

    /**
     * 根據建立時間範圍查詢 Hello 實體
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return Hello 實體列表
     */
    @Query("SELECT h FROM HelloEntity h WHERE h.creationTime >= :startTime AND h.creationTime <= :endTime")
    List<HelloEntity> findByCreationTimeBetween(@Param("startTime") long startTime, @Param("endTime") long endTime);

    /**
     * 分頁查詢所有 Hello 實體
     * @param pageable 分頁參數
     * @return Hello 實體列表
     */
    @Query("SELECT h FROM HelloEntity h ORDER BY h.creationTime DESC")
    List<HelloEntity> findAllWithPagination(Pageable pageable);

    /**
     * 分頁查詢所有 Hello 實體的便利方法
     * @param pageNumber 頁碼
     * @param pageSize 頁面大小
     * @return Hello 實體列表
     */
    default List<HelloEntity> findAllWithPagination(int pageNumber, int pageSize) {
        return findAllWithPagination(PageRequest.of(pageNumber, pageSize));
    }
}