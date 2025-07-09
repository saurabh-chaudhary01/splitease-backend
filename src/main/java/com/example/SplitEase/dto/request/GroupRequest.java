package com.example.SplitEase.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class GroupRequest {
    private String name;
    private Long ownerUserId;
    private List<Long> members;
}
