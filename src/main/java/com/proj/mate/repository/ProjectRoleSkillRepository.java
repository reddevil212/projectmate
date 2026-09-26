package com.proj.mate.repository;

import com.proj.mate.entity.ProjectRoleSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRoleSkillRepository extends JpaRepository<ProjectRoleSkill, Long> {

    // Find all skills required for a specific project role
    List<ProjectRoleSkill> findByProjectRoleId(Long projectRoleId);

    // Find all project roles that require a specific skill
    List<ProjectRoleSkill> findBySkillId(Long skillId);

    // Find a specific skill requirement for a role
    Optional<ProjectRoleSkill> findByProjectRoleIdAndSkillId(
            Long projectRoleId,
            Long skillId
    );

    // Find role-skill requirements by minimum required proficiency
    List<ProjectRoleSkill> findByProficiencyRequiredGreaterThanEqual(int proficiency);

    // Find skills required by a role with a specific proficiency
    List<ProjectRoleSkill> findByProjectRoleIdAndProficiencyRequired(
            Long projectRoleId,
            int proficiency
    );


}