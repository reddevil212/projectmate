package com.proj.mate.repository;

import com.proj.mate.entity.UserSkill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findByUserId(Long userId);

    List<UserSkill> findBySkillId(Long skillId);

    Optional<UserSkill> findByUserIdAndSkillId(Long userId, Long skillId);

    boolean existsByUserIdAndSkillId(Long userId, Long skillId);

    List<UserSkill> findByUserIdAndProficiencyGreaterThanEqual(Long userId, int proficiency);

    List<UserSkill> findBySkillIdAndProficiencyGreaterThanEqual(Long skillId, int proficiency);
}
