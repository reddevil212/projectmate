package com.proj.mate.repository;

import com.proj.mate.entity.Notification;
import com.proj.mate.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {



    List<Notification> findByUser(UserInfo user);


    List<Notification> findByUserOrderByCreatedAtDesc(UserInfo user);


    Optional<Notification> findByIdAndUser(Long id, UserInfo user);

    boolean existsByIdAndUser(Long id, UserInfo user);



    List<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(
            UserInfo user
    );


    List<Notification> findByUserAndIsReadTrueOrderByCreatedAtDesc(
            UserInfo user
    );

    long countByUserAndIsReadFalse(UserInfo user);


    long countByUserAndIsReadTrue(UserInfo user);


    List<Notification> findByUserAndDeletedAtIsNullOrderByCreatedAtDesc(
            UserInfo user
    );

    List<Notification> findByUserAndIsReadFalseAndDeletedAtIsNullOrderByCreatedAtDesc(
            UserInfo user
    );


    List<Notification> findByUserAndIsReadTrueAndDeletedAtIsNullOrderByCreatedAtDesc(
            UserInfo user
    );

    long countByUserAndIsReadFalseAndDeletedAtIsNull(UserInfo user);
}