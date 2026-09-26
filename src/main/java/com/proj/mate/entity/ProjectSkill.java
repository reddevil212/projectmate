
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
 * Entity representing the mapping between Projects and required Skills (`project_skill` table).
 *
 * <p>Contains information on tech stack requirements for a project:</p>
 * <ul>
 *   <li><b>id:</b> Primary key auto-generated ID.</li>
 *   <li><b>project:</b> Reference to {@link Project} requiring the skill.</li>
 *   <li><b>skill:</b> Reference to {@link Skill} required.</li>
 *   <li><b>level:</b> Required experience/proficiency level (e.g., Beginner, Intermediate, Expert).</li>
 * </ul>
 *
 * <p><b>Used for:</b> Filtering and searching projects based on overall tech stack requirements.</p>
 */


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "project_skill")
public class ProjectSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
    @Column(name = "level")
    private String level;

}
