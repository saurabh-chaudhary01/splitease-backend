package com.example.SplitEase.controller;

import com.example.SplitEase.dto.request.ExpenseRequest;
import com.example.SplitEase.dto.request.GroupRequest;
import com.example.SplitEase.dto.response.GroupResponse;
import com.example.SplitEase.dto.response.SettlementResponse;
import com.example.SplitEase.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Group APIs", description = "APIs for creating groups and managing group expenses")
public class GroupController {
    private final GroupService groupService;

    @Operation(
            summary = "Create a new group",
            description = "Creates a group with the given details"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponse createGroup(@Valid @RequestBody GroupRequest dto) {
        return groupService.createGroup(dto);
    }

    @Operation(
            summary = "Add an expense to a group",
            description = "Adds a new expense to the specified group"
    )
    @PostMapping("/{groupId}/expense")
    public void addExpense(@PathVariable("groupId") Long groupId, @Valid @RequestBody ExpenseRequest dto) {
        groupService.addExpense(groupId, dto);
    }

    @Operation(
            summary = "Removes an expense to a group",
            description = "Removes a new expense to the specified group"
    )
    @DeleteMapping("/{groupId}/expense/{expenseId}")
    public void removeExpense(@PathVariable("groupId") Long groupId, @PathVariable("expenseId") Long expenseId) {
        groupService.removeExpense(groupId, expenseId);
    }

    @Operation(
            summary = "Get group expense result",
            description = "Returns net balances for all group members"
    )
    @GetMapping("/{groupId}/expense/result")
    public List<SettlementResponse> getGroupExpenseSummary(@PathVariable("groupId") Long groupId) {
        return groupService.getExpenseSummary(groupId);
    }
}
