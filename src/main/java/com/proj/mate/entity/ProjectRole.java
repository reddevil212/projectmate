/**
 * Entity representing a Project Role in the database (`project_role` table).
 *
 * <p>Contains information related to open team roles/positions within projects:</p>
 * <ul>
 *   <li><b>id:</b> Primary key auto-generated ID.</li>
 *   <li><b>name:</b> Name/Title of the role (e.g., Frontend Engineer, UI/UX Designer, DevOps).</li>
 * </ul>
 *
 * <p><b>Used for:</b> Which project requires which roles.</p>
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
@Table(name = "project_role")
public class ProjectRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
}
