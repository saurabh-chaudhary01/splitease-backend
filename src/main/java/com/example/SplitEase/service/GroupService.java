package com.example.SplitEase.service;

import com.example.SplitEase.dto.request.ExpenseRequest;
import com.example.SplitEase.dto.request.GroupRequest;
import com.example.SplitEase.dto.response.ExpenseResult;
import com.example.SplitEase.dto.response.GroupResponse;
import com.example.SplitEase.dto.response.UserResponse;
import com.example.SplitEase.entity.ExpenseEntity;
import com.example.SplitEase.entity.GroupEntity;
import com.example.SplitEase.entity.SplitEntity;
import com.example.SplitEase.entity.UserEntity;
import com.example.SplitEase.repository.GroupRepository;
import com.example.SplitEase.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<ExpenseResult> getGroupExpense(Long groupId) {
        GroupEntity groupEntity = groupRepository.findById(groupId).orElseThrow();

        Map<UserEntity, Double> userCostMap = new HashMap<>();
        for (ExpenseEntity expense : groupEntity.getExpenses()) {
            UserEntity payer = expense.getPaidBy();
            Double amount = expense.getAmount();
            if (amount == null) amount = 0.0;

            updateUserCostMap(userCostMap, payer, amount);

            String splitOption = expense.getSplitOption();
            List<SplitEntity> splits = expense.getSplits();
            int memberSize = splits.size();

            if (Objects.equals(splitOption, "equally")) {
                for (SplitEntity split : splits) {
                    updateUserCostMap(userCostMap, split.getUser(), -1 * split.getAmount() / memberSize);
                }
            } else if (Objects.equals(splitOption, "percentage")) {
                for (SplitEntity split : splits) {
                    updateUserCostMap(userCostMap, split.getUser(), -1 * (split.getAmount() * split.getPercentage()) / 100);
                }
            } else {
                long shareSize = 0;
                for (SplitEntity split : splits) {
                    shareSize += split.getShare();
                }

                for (SplitEntity split : splits) {
                    updateUserCostMap(userCostMap, split.getUser(), -1 * (split.getAmount() * split.getShare()) / shareSize);
                }
            }
        }

        List<Map.Entry<UserEntity, Double>> tempList = new ArrayList<>(userCostMap.entrySet().stream().toList());
        tempList.sort(Comparator.comparingDouble(Map.Entry::getValue));

        List<List<Object>> userCostList = tempList.stream().map(item -> Arrays.asList(item.getKey(), item.getValue())).toList();

        int start = 0;
        int end = userCostList.size() - 1;
        List<ExpenseResult> expenseResults = new ArrayList<>();
        while (start < end) {
            List<Object> obj1 = userCostList.get(start);
            List<Object> obj2 = userCostList.get(end);

            ExpenseResult expenseResult = new ExpenseResult();
            expenseResult.setPayer(UserResponse.convertToDTO((UserEntity) obj1.getFirst()));
            expenseResult.setPayee(UserResponse.convertToDTO((UserEntity) obj2.getFirst()));

            double amount1 = Math.abs((double) obj1.getLast());
            double amount2 = (double) obj2.getLast();

            if (amount1 <= amount2) {
                expenseResult.setAmount(amount1);
                start++;

                amount2 -= amount1;
                obj2.set(1, amount2);
                if (amount2 < 0.0) end--;
            } else {
                expenseResult.setAmount(amount2);
                end--;

                amount1 -= amount2;
                obj1.set(1, -1 * amount1);
            }

            expenseResults.add(expenseResult);
        }

        return expenseResults;
    }

    private void updateUserCostMap(Map<UserEntity, Double> map, UserEntity user, Double amount) {
        Double prevAmount = map.get(user);
        if (prevAmount == null) prevAmount = 0.0;
        map.put(user, prevAmount + amount);
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
