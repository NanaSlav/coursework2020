package ru.nanaslav.planner.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Task entity
 * Contains information about task
 * @author NanaSlav
 */
@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private boolean isDone;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(
            mappedBy = "task",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Attachment> attachments;

    @OneToMany(
            mappedBy = "task",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments;

    public Task(String name, String description, Project project) {
        this.name = name;
        this.description = description;
        this.project = project;
    }

    public Task() {}
}
