package ru.nanaslav.planner.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Project entity
 * Contains information about project
 * @author NanaSlav
 */
@Entity
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Task> tasks;

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Participant> participants = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    private Account creator;

    public Project(String name, String description, Account creator) {
        this.name = name;
        this.description = description;
        this.creator = creator;
    }

    public Project() {}

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }
}
