/**
 * Entity representing Refresh Tokens in the database (`refresh_token` table).
 *
 * <p>Contains details for JWT session management:</p>
 * <ul>
 *   <li><b>id:</b> Primary key auto-generated ID.</li>
 *   <li><b>user:</b> One-to-one reference to {@link UserInfo} owning this token.</li>
 *   <li><b>token:</b> Unique refresh token string (UUID/Hash).</li>
 *   <li><b>expiryDate:</b> Expiration timestamp for session validity.</li>
 * </ul>
 *
 * <p><b>Used for:</b> Issuing new JWT access tokens without requiring re-authentication.</p>
 */
package com.proj.mate.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserInfo user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}
