package com.proj.mate.repository;

import com.proj.mate.entity.ProjectSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, Long> {

    // Find all skills required by a project
    List<ProjectSkill> findByProjectId(Long projectId);

    // Find all projects requiring a particular skill
    List<ProjectSkill> findBySkillId(Long skillId);

    // Find a specific skill requirement for a project
    Optional<ProjectSkill> findByProjectIdAndSkillId(Long projectId, Long skillId);

    // Find project skill requirements by required level
    List<ProjectSkill> findByLevel(String level);

    // Find skills required by a project at a specific level
    List<ProjectSkill> findByProjectIdAndLevel(Long projectId, String level);
}
