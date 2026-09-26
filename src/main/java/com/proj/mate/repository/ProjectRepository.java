package com.proj.mate.repository;

import com.proj.mate.entity.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwnerId(Long userId);

    List<Project> findByStatus(String status);

    List<Project> findByType(String type);

    List<Project> findByOwnerIdAndStatus(Long userId, String status);

    Optional<Project> findByName(String name);

    List<Project> findByNameContainingIgnoreCase(String name);

    List<Project> findByTypeAndStatus(String type, String status);


}