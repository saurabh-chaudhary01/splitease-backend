package com.example.SplitEase.service;

import com.example.SplitEase.dto.request.ExpenseRequest;
import com.example.SplitEase.dto.request.GroupRequest;
import com.example.SplitEase.dto.response.GroupResponse;
import com.example.SplitEase.dto.response.SettlementResponse;

import java.util.List;

public interface GroupService {

    GroupResponse createGroup(GroupRequest dto);

    void addExpense(Long groupId, ExpenseRequest dto);

    void removeExpense(Long groupId, Long expenseId);

    List<SettlementResponse> getExpenseSummary(Long groupId);

    List<GroupResponse> getGroupsOwnedBy(Long userId);

    List<GroupResponse> getGroupsParticipatedBy(Long userId);
}