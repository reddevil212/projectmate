package com.proj.mate.repository;
import com.proj.mate.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String email);
    List<UserInfo> findByRole(String role);
    List<UserInfo> findByRoleIn(List<String> roles);
    List<UserInfo> findByRoleNotIn(List<String> roles);
    List<UserInfo> findByNameContainingIgnoreCase(String name);

}
