package ru.nanaslav.planner.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Participant entity
 * Contains information about concrete project participant
 * @author NanaSlav
 */
@Entity
@Data
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Account participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Participant(Account participant, Project project, Role role) {
        this.participant = participant;
        this.project = project;
        this.role = role;
    }

    public Participant() {}
}
