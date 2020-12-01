package ru.nanaslav.planner.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private List<Task> tasks;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id")
    private Account creator;

    public Project(String name, String description, Account creator) {
        this.name = name;
        this.description = description;
        this.creator = creator;
    }
}
