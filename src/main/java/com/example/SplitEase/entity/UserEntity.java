package com.example.SplitEase.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String emailId;

    private String firstName;
    private String lastName;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
