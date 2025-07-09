package com.example.SplitEase.controller;

import com.example.SplitEase.dto.request.ExpenseRequest;
import com.example.SplitEase.dto.request.GroupRequest;
import com.example.SplitEase.dto.response.GroupResponse;
import com.example.SplitEase.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/create-group")
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponse createGroup(@RequestBody GroupRequest dto) {
        return groupService.createGroup(dto);
    }

    @PostMapping("/add-expense")
    public void addExpense(@RequestBody ExpenseRequest dto) {
        groupService.addExpense(dto);
    }

    @GetMapping("/user-groups/{userId}")
    public List<GroupResponse> getUserGroups(@PathVariable Long userId) {
        return groupService.getUserGroups(userId);
    }

    @GetMapping("/member-groups/{userId}")
    public List<GroupResponse> getUserMemberGroups(@PathVariable Long userId) {
        return groupService.getUserMemberGroups(userId);
    }
}
