package com.example.SplitEase.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class SplitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private Integer share;
    private Integer percentage;

    @ManyToOne
    private UserEntity owedBy;
}
