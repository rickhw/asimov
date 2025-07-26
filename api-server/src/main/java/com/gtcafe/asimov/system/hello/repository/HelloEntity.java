package com.gtcafe.asimov.system.hello.repository;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Hello 資料庫實體類別
 * 用於持久化 Hello 相關資料
 */
@Entity
@Table(name = "hello")
@Data
@NoArgsConstructor
public class HelloEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "creation_time")
    private long creationTime;

    @Column(name = "updated_time")
    private long updatedTime;

    public HelloEntity(String message) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
        long currentTime = System.currentTimeMillis();
        this.creationTime = currentTime;
        this.updatedTime = currentTime;
    }
}