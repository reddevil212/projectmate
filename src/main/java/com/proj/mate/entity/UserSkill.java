package com.proj.mate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**

 Entity representing a user's skill profile in the database.

 <p>This entity maps users to the skills they possess and stores

 their proficiency level for each skill.</p>

 <p>The corresponding database table is {@code user_skill}.</p>
 <ul>
 <li>{@code id}: Primary key with an auto-generated ID.</li>
 <li>{@code user}: Reference to the {@link UserInfo} who possesses the skill.</li>
 <li>{@code skill}: Reference to the {@link Skill} possessed by the user.</li>
 <li>{@code proficiency}: Numerical rating representing the user's skill level.</li>
 </ul>
 <p>A user can have multiple skills, but the same skill cannot be

 associated with the same user more than once.</p>

 @author Sayan Pal
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "user_skill",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "skill_id"})
        }
)
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "proficiency", nullable = false)
    private int proficiency;
}