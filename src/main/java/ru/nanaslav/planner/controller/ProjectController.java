package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Participant;
import ru.nanaslav.planner.model.Project;
import ru.nanaslav.planner.model.Role;
import ru.nanaslav.planner.repository.AccountRepository;
import ru.nanaslav.planner.repository.ParticipantRepository;
import ru.nanaslav.planner.repository.ProjectRepository;
import ru.nanaslav.planner.service.ProjectService;

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
    AccountRepository accountRepository;
    @Autowired
    ProjectService projectService;

    @GetMapping("/{projectId}")
    public String showProjectPage(@PathVariable long projectId,
                                  Model model) {
        Project project = projectRepository.findById(projectId).orElseThrow(IllegalStateException::new);
        model.addAttribute("name", project.getName());
        model.addAttribute("description", project.getDescription());
        model.addAttribute("tasks", project.getTasks());
        return "project-details";
    }

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
        projectService.createProject(projectName, description, account);
        // TODO: add view - project list
        return "redirect:/home";
    }

    @GetMapping("/add")
    public String showAddProject() {
        return "add-project";
    }

    @PostMapping("/{projectId}/add-participant")
    public String addParticipant(@PathVariable long projectId,
                                 @RequestParam String participantName) {
        Account account = accountRepository.findByUsername(participantName);
        if (account != null) {
            projectService.addParticipant(projectId, account);
        } else {
            // TODO: return message - no user
            return "";
        }
        // TODO: add view - participants list
        return "";
    }
}
