package com.example.SplitEase.service.impl;

import com.example.SplitEase.dto.request.ExpenseRequest;
import com.example.SplitEase.dto.request.GroupRequest;
import com.example.SplitEase.dto.response.GroupResponse;
import com.example.SplitEase.dto.response.SettlementResponse;
import com.example.SplitEase.dto.response.UserResponse;
import com.example.SplitEase.entity.ExpenseEntity;
import com.example.SplitEase.entity.GroupEntity;
import com.example.SplitEase.entity.SplitEntity;
import com.example.SplitEase.entity.UserEntity;
import com.example.SplitEase.exception.GroupNotFoundException;
import com.example.SplitEase.exception.UserNotFoundException;
import com.example.SplitEase.repository.GroupRepository;
import com.example.SplitEase.repository.UserRepository;
import com.example.SplitEase.service.GroupService;
import com.example.SplitEase.utility.Pair;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public GroupResponse createGroup(GroupRequest dto) {
        UserEntity userEntity = userRepository
                .findById(dto.getOwnerUserId())
                .orElseThrow(() -> new UserNotFoundException("invalid ownerUserId " + dto.getOwnerUserId()));

        GroupEntity groupEntity = new GroupEntity();

        groupEntity.setName(dto.getName());
        groupEntity.setOwner(userEntity);

        dto.getMembers().forEach(memberId -> {
            UserEntity member = userRepository.findById(memberId)
                    .orElseThrow(() -> new UserNotFoundException("invalid memberId " + memberId));
            groupEntity.getMembers().add(member);
        });

        GroupEntity persistedGroup = groupRepository.save(groupEntity);

        return GroupResponse.convertToDTO(persistedGroup);
    }

    @Transactional
    public void addExpense(Long groupId, ExpenseRequest dto) {
        ExpenseEntity expenseEntity = new ExpenseEntity();

        expenseEntity.setAmount(dto.getAmount());
        expenseEntity.setDescription(dto.getDescription());
        expenseEntity.setSplitOption(dto.getSplitOption());

        UserEntity payer = userRepository.findById(dto.getPayerId())
                .orElseThrow(() -> new UserNotFoundException("invalid payerId " + dto.getPayerId()));
        expenseEntity.setPayer(payer);

        List<SplitEntity> splits = dto.getSplits()
                .stream()
                .map(splitDto -> {
                    SplitEntity split = new SplitEntity();
                    split.setAmount(splitDto.getAmount());
                    split.setShare(splitDto.getShare());
                    split.setPercentage(splitDto.getPercentage());

                    UserEntity paidBy = userRepository.findById(splitDto.getPaidByUserId())
                            .orElseThrow(() -> new UserNotFoundException("invalid paidByUserId " + splitDto.getPaidByUserId()));
                    split.setOwedBy(paidBy);

                    return split;
                }).toList();

        expenseEntity.setSplits(splits);

        GroupEntity groupEntity = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("invalid groupId " + groupId));

        groupEntity.getExpenses().add(expenseEntity);
    }

    @Transactional
    public void removeExpense(Long groupId, Long expenseId) {
        GroupEntity groupEntity = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("invalid groupId " + groupId));

        for (ExpenseEntity expense : groupEntity.getExpenses()) {
            if (expense.getId().equals(expenseId)) {
                groupEntity.getExpenses().remove(expense);
                break;
            }
        }
    }

    public List<SettlementResponse> getExpenseSummary(Long groupId) {
        GroupEntity groupEntity = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("invalid groupId " + groupId));

        UserEntity owner = groupEntity.getOwner();
        List<UserEntity> members = groupEntity.getMembers();
        List<ExpenseEntity> expenses = groupEntity.getExpenses();

        Map<Long, UserResponse> userIdMap = new HashMap<>();

        userIdMap.put(owner.getId(), UserResponse.convertToDTO(owner));
        members.forEach(member -> userIdMap
                .put(member.getId(), UserResponse.convertToDTO(member)));

        Map<Long, Double> costMap = new HashMap<>();

        expenses.forEach(expense -> {
            Long payerId = expense.getPayer().getId();
            Double amount = expense.getAmount();
            List<SplitEntity> splits = expense.getSplits();

            costMap.put(payerId, costMap.getOrDefault(payerId, 0.0) + amount);

            splits.forEach(split -> {
                Long owedById = split.getOwedBy().getId();
                costMap.put(owedById, costMap.getOrDefault(owedById, 0.0) - split.getAmount());
            });
        });

        ArrayList<Pair<Long, Double>> toPay = new ArrayList<>();
        ArrayList<Pair<Long, Double>> toReceive = new ArrayList<>();

        costMap.forEach((userId, amount) -> {
            if (amount == 0.0) return;

            if (amount < 0) {
                toPay.add(new Pair<>(userId, Math.abs(amount)));
            } else {
                toReceive.add(new Pair<>(userId, Math.abs(amount)));
            }
        });

        List<SettlementResponse> settlements = new ArrayList<>();

        int i = 0, j = 0;
        while (i < toPay.size() && j < toReceive.size()) {
            long payerId = toPay.get(i).getKey();
            long receiverId = toReceive.get(i).getKey();

            double payAmount = toPay.get(i).getValue();
            double receiveAmount = toReceive.get(i).getValue();
            double settlementAmount = Math.min(payAmount, receiveAmount);

            settlements.add(SettlementResponse.builder()
                    .from(userIdMap.get(payerId))
                    .to(userIdMap.get(receiverId))
                    .amount(settlementAmount)
                    .build()
            );

            payAmount -= settlementAmount;
            receiveAmount -= settlementAmount;

            toPay.get(i).setValue(payAmount);
            toReceive.get(i).setValue(receiveAmount);

            if (payAmount == 0.0) i++;
            if (receiveAmount == 0.0) j++;
        }

        return settlements;
    }

    public List<GroupResponse> getGroupsOwnedBy(Long userId) {
        List<GroupEntity> groups = groupRepository.findByOwner_Id(userId);
        return groups.stream()
                .map(GroupResponse::convertToDTO)
                .toList();
    }

    public List<GroupResponse> getGroupsParticipatedBy(Long userId) {
        List<GroupEntity> groups = groupRepository.findByMembers_Id(userId);
        return groups.stream()
                .map(GroupResponse::convertToDTO)
                .toList();
    }
}
