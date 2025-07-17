package com.example.SplitEase.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private UserEntity owner;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<UserEntity> members = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExpenseEntity> expenses = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
}
