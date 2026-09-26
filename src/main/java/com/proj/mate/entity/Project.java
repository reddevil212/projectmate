/**
 * Entity representing a Project in the database (`project` table).
 *
 * <p>Contains information related to collaborative projects created by users:</p>
 * <ul>
 *   <li><b>id:</b> Primary key auto-generated ID.</li>
 *   <li><b>owner:</b> Many-to-one reference to {@link UserInfo} (project creator/owner).</li>
 *   <li><b>name:</b> Title/Name of the project.</li>
 *   <li><b>type:</b> Domain/Category of the project (e.g., Web, Mobile, AI).</li>
 *   <li><b>description:</b> Detailed summary of project goals and scope.</li>
 *   <li><b>memberCount:</b> Target or current capacity of team members.</li>
 *   <li><b>createdAt:</b> Timestamp when the project was posted.</li>
 *   <li><b>status:</b> Current lifecycle status (e.g., OPEN, IN_PROGRESS, COMPLETED).</li>
 * </ul>
 *
 * <p><b>Used for:</b> Central entity for project listings, member matching, and project management.</p>
 */
package com.proj.mate.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo owner;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @Column(name = "description")
    private String description;
    @Column(name = "member_count")
    private int memberCount;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "status")
    private String status;

    @jakarta.persistence.PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
