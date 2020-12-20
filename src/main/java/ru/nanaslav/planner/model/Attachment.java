package ru.nanaslav.planner.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;

}
