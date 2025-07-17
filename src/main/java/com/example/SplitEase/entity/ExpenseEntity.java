package com.example.SplitEase.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
public class ExpenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String description;
    private String splitOption;

    @ManyToOne
    private UserEntity payer;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SplitEntity> splits = new ArrayList<>();
}