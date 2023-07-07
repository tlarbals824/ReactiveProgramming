package com.sim.r2dbc.common.repository;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("AUTH")
@Data
public class AuthEntity {
    @Id
    private Long id;
    private final Long userId;
    private final String token;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PersistenceCreator
    public AuthEntity(Long id, Long userId, String token) {
        this.id = id;
        this.userId = userId;
        this.token = token;
    }

    public AuthEntity(Long userId, String token) {
        this(null, userId, token);
    }
}
