/**
 * Entity representing a master Skill in the database (`skill` table).
 *
 * <p>Contains information for standard technical and domain skills:</p>
 * <ul>
 *   <li><b>id:</b> Primary key auto-generated ID.</li>
 *   <li><b>name:</b> Name of the skill (e.g., Java, React, Docker, PostgreSQL).</li>
 * </ul>
 *
 * <p><b>Used for:</b> Master skill lookup catalog referenced across users, projects, and roles.</p>
 */
package com.proj.mate.entity;


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
@Table(name = "skill")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
}
