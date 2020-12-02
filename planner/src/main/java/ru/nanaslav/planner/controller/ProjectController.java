package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Participant;
import ru.nanaslav.planner.model.Project;
import ru.nanaslav.planner.model.Role;
import ru.nanaslav.planner.repository.AccountRepository;
import ru.nanaslav.planner.repository.ParticipantRepository;
import ru.nanaslav.planner.repository.ProjectRepository;

import java.util.Optional;

/**
 * Controller for project
 * Manage all project information
 * @author NanaSlav
 */
@Controller
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    AccountRepository accountRepository;

    /**
     * Create new project by current user
     * @param projectName
     * @param description
     * @param account current user account
     * @return view name
     */
    @PostMapping("/add")
    public String addProject(@RequestParam String projectName,
                             @RequestParam String description,
                             @AuthenticationPrincipal Account account) {

        Project project = new Project(projectName, description, account);
        Participant participant = new Participant(account, project, Role.CREATOR);
        project.addParticipant(participant);
        projectRepository.save(project);
        participantRepository.save(participant);

        // TODO: add view - project list
        return "main";
    }

    @GetMapping("/add")
    public String showAddProject() {
        // TODO: add view - add project
        return "add-project";
    }

    @PostMapping("/{projectId}/add-participant")
    public String addParticipant(@PathVariable long projectId,
                                 @RequestParam String participantName) {
        Project project = projectRepository.findById(projectId).orElseThrow(IllegalStateException::new);
        Account account = accountRepository.findByUsername(participantName);
        if (account != null) {
            Participant participant = new Participant(account, project, Role.PARTICIPANT);
            participantRepository.save(participant);
            project.addParticipant(participant);
            projectRepository.save(project);
        } else {
            // TODO: return message - no user
            return "";
        }

        // TODO: add view - participants list
        return "";
    }

}
