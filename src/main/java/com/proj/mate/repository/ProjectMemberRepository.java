package com.proj.mate.repository;

import com.proj.mate.entity.Project;
import com.proj.mate.entity.ProjectMember;
import com.proj.mate.entity.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    Optional<ProjectMember> findByProjectAndUser(Project project, UserInfo user);

    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);

    List<ProjectMember> findByProject(Project project);

    List<ProjectMember> findByProjectId(Long projectId);

    List<ProjectMember> findByUser(UserInfo user);

    List<ProjectMember> findByUserId(Long userId);

    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
}
