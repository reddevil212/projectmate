package com.proj.mate.repository;


import com.proj.mate.entity.RefreshToken;
import com.proj.mate.entity.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(UserInfo user);

    @Modifying
    int deleteByUser(UserInfo user);
}
