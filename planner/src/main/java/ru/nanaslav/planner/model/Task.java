package ru.nanaslav.planner.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;

    public Task(String name, String description, Project project) {
        this.name = name;
        this.description = description;
        this.project = project;
    }
}
