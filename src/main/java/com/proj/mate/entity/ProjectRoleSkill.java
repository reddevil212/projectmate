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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing the junction mapping between Project Roles and Skills (`project_role_skill` table).
 *
 * <p>Contains details linking a specific project role to its required skill:</p>
 * <ul>
 *   <li><b>id:</b> Primary key auto-generated ID.</li>
 *   <li><b>projectRole:</b> Reference to {@link ProjectRole} requiring this skill.</li>
 *   <li><b>skill:</b> Reference to {@link Skill} required by the role.</li>
 * </ul>
 *
 * <p><b>Used for:</b> which role requires which skills, and which skills are required by which roles.</p>
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "project_role_skill")
public class ProjectRoleSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_role_id", nullable = false)
    private ProjectRole projectRole;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
    @Column(name = "proficiency_required", nullable = false)
    private int proficiencyRequired;
}
