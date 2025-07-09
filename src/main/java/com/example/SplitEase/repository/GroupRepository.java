package com.example.SplitEase.repository;

import com.example.SplitEase.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    List<GroupEntity> findByMembers_Id(Long userId);

    List<GroupEntity> findByOwner_Id(Long userId);
}
