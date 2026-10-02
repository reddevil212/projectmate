package com.proj.mate.repository;

import com.proj.mate.entity.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwnerId(Long userId);

    List<Project> findByStatus(String status);

    List<Project> findByType(String type);

    List<Project> findByVisibility(String visibility);

    List<Project> findByOwnerIdAndStatus(Long userId, String status);

    Optional<Project> findByName(String name);

    List<Project> findByNameContainingIgnoreCase(String name);

    List<Project> findByTypeAndStatus(String type, String status);

    List<Project> findByTypeAndVisibility(String type, String visibility);

    List<Project> findByStatusAndVisibility(String status, String visibility);
}
