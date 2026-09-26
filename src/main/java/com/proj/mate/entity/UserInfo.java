/**
 * Entity representing User Accounts in the database (`user_info` table).
 *
 * <p>Contains core personal and authentication details of platform users:</p>
 * <ul>
 *   <li><b>id:</b> Primary key auto-generated ID.</li>
 *   <li><b>name:</b> Full name of the user.</li>
 *   <li><b>email:</b> User's email address (used for login).</li>
 *   <li><b>password:</b> BCrypt encrypted password hash.</li>
 *   <li><b>createdAt:</b> Account creation timestamp.</li>
 * </ul>
 *
 * <p><b>Used for:</b> Authentication, user profile management, and ownership of projects/skills.</p>
 */
package com.proj.mate.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
