package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Participant;
import ru.nanaslav.planner.model.Project;
import ru.nanaslav.planner.repository.AccountRepository;
import ru.nanaslav.planner.repository.ProjectRepository;
import ru.nanaslav.planner.service.AccountService;
import ru.nanaslav.planner.service.ProjectService;

import java.util.List;

/**
 * Controller for project
 * Manage all project information
 * @author NanaSlav
 */
@Controller
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    AccountService accountService;
    @Autowired
    ProjectService projectService;

    @GetMapping("/")
    public String showProjectsList(@AuthenticationPrincipal Account account, Model model) {
        List<Project> projects = projectService.getProjectsByAccount(account);
        model.addAttribute("projects", projects);
        return "project/projects-list";
    }

    @GetMapping("/{projectId}")
    public String showProjectPage(@PathVariable long projectId,
                                  Model model) {
        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);
        return "project/project-details";
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
        long id = projectService.createProject(projectName, description, account);
        return "redirect:/projects/" + id;
    }

    @GetMapping("/add")
    public String showAddProject() {
        return "project/add-project";
    }

    @PostMapping("/{projectId}/add-participant")
    public String addParticipant(@PathVariable long projectId,
                                 @RequestParam String participantName) {
        Account account = accountService.getAccountByUsername(participantName);
        if (account != null) {
            projectService.addParticipant(projectId, account);
        } else {
            // TODO: return message - no user
            return "";
        }
        // TODO: add view - participants list
        return "";
    }

    @GetMapping("/edit/{projectId}")
    public String showEditForm(@PathVariable long projectId, Model model) {
        Project project = projectService.getProjectById(projectId);
        model.addAttribute("name", project.getName());
        model.addAttribute("description", project.getDescription());
        model.addAttribute("id", project.getId());
        return "project/edit-project";
    }

    @PostMapping("/edit/{projectId}")
    public String editProject(@PathVariable long projectId,
                              @RequestParam String name,
                              @RequestParam String description) {
        projectService.editProject(projectId, name, description);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/delete/{projectId}")
    public String deleteProject(@PathVariable long projectId) {
        projectService.delete(projectId);
       return "redirect:/projects/";
    }
}
