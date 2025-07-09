package com.example.SplitEase.controller;

import com.example.SplitEase.dto.request.ExpenseRequest;
import com.example.SplitEase.dto.request.GroupRequest;
import com.example.SplitEase.dto.response.GroupResponse;
import com.example.SplitEase.entity.UserPrincipal;
import com.example.SplitEase.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponse createGroup(@RequestBody GroupRequest dto) {
        return groupService.createGroup(dto);
    }

    @PostMapping("/expense")
    public void addExpense(@RequestBody ExpenseRequest dto) {
        groupService.addExpense(dto);
    }

    @GetMapping("/user-groups")
    public List<GroupResponse> getUserGroups(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal.getUserId();
        return groupService.getUserGroups(userId);
    }

    @GetMapping("/member-groups")
    public List<GroupResponse> getUserMemberGroups(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal.getUserId();
        return groupService.getUserMemberGroups(userId);
    }
}
