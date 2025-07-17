package com.example.SplitEase.controller;

import com.example.SplitEase.dto.response.GroupResponse;
import com.example.SplitEase.dto.response.UserResponse;
import com.example.SplitEase.entity.UserPrincipal;
import com.example.SplitEase.service.GroupService;
import com.example.SplitEase.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User APIs", description = "Endpoints related to the current authenticated user and its group")
public class UserController {
    private final UserService userService;
    private final GroupService groupService;

    @Operation(
            summary = "Get current user profile",
            description = "Returns the profile of the currently authenticated user"
    )
    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse userProfile(@AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getUsername();
        return userService.getUserByEmailId(email);
    }

    @Operation(
            summary = "Get groups owned by current user",
            description = "Returns all groups created by the authenticated user"
    )
    @GetMapping("/groups/owned")
    public List<GroupResponse> getUserGroups(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal.getId();
        return groupService.getGroupsOwnedBy(userId);
    }

    @Operation(
            summary = "Get groups user is a member of",
            description = "Returns all groups where the user is a member"
    )
    @GetMapping("/groups/participating")
    public List<GroupResponse> getUserMemberGroups(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal.getId();
        return groupService.getGroupsParticipatedBy(userId);
    }
}
