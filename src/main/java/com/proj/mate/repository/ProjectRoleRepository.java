package com.proj.mate.repository;

import com.proj.mate.entity.ProjectRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long> {

    Optional<ProjectRole> findByName(String name);

    Optional<ProjectRole> findByNameIgnoreCase(String name);
}
