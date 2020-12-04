package ru.nanaslav.planner.model;

/**
 * Role of the participant in project
 * Role defines participant privileges
 * @author NanaSlav
 */
public enum Role {
    /** Creator of the project. All access */
    CREATOR,
    /** Project participant. Can only carry out tasks */
    PARTICIPANT
}
