package com.sim.r2dbc.common.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@AllArgsConstructor
@Table("USER")
@Data
public class UserEntity {
    @Id
    private final String id;
    private final String name;
    private final Integer age;
    private final String profileImageId;
    private final String password;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PersistenceCreator
    public UserEntity(String id, String name, Integer age, String profileImageId, String password) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.profileImageId = profileImageId;
        this.password = password;
    }

    public UserEntity(String name, Integer age, String profileImageId, String password) {
        this(null, name, age, profileImageId, password);
    }
}
