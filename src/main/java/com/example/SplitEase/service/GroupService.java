package com.example.SplitEase.service;

import com.example.SplitEase.dto.request.ExpenseRequest;
import com.example.SplitEase.dto.request.GroupRequest;
import com.example.SplitEase.dto.response.GroupResponse;
import com.example.SplitEase.entity.ExpenseEntity;
import com.example.SplitEase.entity.GroupEntity;
import com.example.SplitEase.entity.SplitEntity;
import com.example.SplitEase.entity.UserEntity;
import com.example.SplitEase.repository.GroupRepository;
import com.example.SplitEase.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public GroupResponse createGroup(GroupRequest dto) {
        UserEntity userEntity = userRepository.findById(dto.getOwnerUserId())
                .orElseThrow();

        GroupEntity groupEntity = new GroupEntity();

        groupEntity.setName(dto.getName());
        groupEntity.setOwner(userEntity);

        dto.getMembers().forEach(userId -> {
            UserEntity member = userRepository.findById(userId).orElse(null);
            if (!Objects.isNull(member)) {
                groupEntity.getMembers().add(member);
            }
        });

        GroupEntity persistedGroup = groupRepository.save(groupEntity);

        return GroupResponse.convertToDTO(persistedGroup);
    }

    public void addExpense(ExpenseRequest dto) {
        ExpenseEntity expenseEntity = new ExpenseEntity();

        expenseEntity.setAmount(dto.getAmount());
        expenseEntity.setDescription(dto.getDescription());
        expenseEntity.setSplitOption(dto.getSplitOption());

        UserEntity payer = userRepository.findById(dto.getUserId()).orElseThrow();
        expenseEntity.setPaidBy(payer);

        List<SplitEntity> splits = dto.getSplits().stream().map(splitDto -> {
            SplitEntity split = new SplitEntity();
            split.setAmount(splitDto.getAmount());
            split.setShare(splitDto.getShare());
            split.setPercentage(splitDto.getPercentage());

            UserEntity splitUserEntity = userRepository.findById(splitDto.getUserId()).orElseThrow();
            split.setUser(splitUserEntity);

            return split;
        }).toList();

        expenseEntity.setSplits(splits);

        GroupEntity groupEntity = groupRepository.findById(dto.getGroupId()).orElseThrow();
        groupEntity.getExpenses().add(expenseEntity);
    }

    public List<GroupResponse> getUserGroups(Long userId) {
        List<GroupEntity> groups = groupRepository.findByOwner_Id(userId);
        return groups.stream()
                .map(GroupResponse::convertToDTO)
                .toList();
    }

    public List<GroupResponse> getUserMemberGroups(Long userId) {
        List<GroupEntity> groups = groupRepository.findByMembers_Id(userId);
        return groups.stream()
                .map(GroupResponse::convertToDTO)
                .toList();
    }
}
