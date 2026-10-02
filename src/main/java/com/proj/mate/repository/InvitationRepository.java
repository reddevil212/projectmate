package com.proj.mate.repository;

import com.proj.mate.entity.Invitation;
import com.proj.mate.entity.Project;
import com.proj.mate.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findByUserOrderByCreatedAtDesc(UserInfo user);

    List<Invitation> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Invitation> findByReceiverOrderByCreatedAtDesc(UserInfo receiver);

    List<Invitation> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    List<Invitation> findByReceiverAndStatusOrderByCreatedAtDesc(UserInfo receiver, String status);

    List<Invitation> findByReceiverIdAndStatusOrderByCreatedAtDesc(Long receiverId, String status);

    List<Invitation> findByProjectOrderByCreatedAtDesc(Project project);

    List<Invitation> findByProjectIdOrderByCreatedAtDesc(Long projectId);

    Optional<Invitation> findByProjectIdAndReceiverIdAndStatus(Long projectId, Long receiverId, String status);

    boolean existsByProjectIdAndReceiverIdAndStatus(Long projectId, Long receiverId, String status);

    long countByReceiverIdAndStatus(Long receiverId, String status);
}
