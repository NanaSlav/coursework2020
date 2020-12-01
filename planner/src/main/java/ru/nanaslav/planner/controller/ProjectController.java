package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Project;
import ru.nanaslav.planner.repository.ProjectRepository;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    ProjectRepository projectRepository;

    @PostMapping("/add")
    public String addProject(@RequestParam String projectName,
                             @RequestParam String description,
                             @AuthenticationPrincipal Account account) {

        Project project = new Project(projectName, description, account);
        if (projectRepository.findByNameAndCreator(projectName, account) != null) {
            project.setName(projectName + "#" + project.getId());
        }
        projectRepository.save(project);
        // TODO: add view - project list
        return "projects";
    }

    @GetMapping("/add")
    public String showAddProject() {
        // TODO: add view - add project
        return "add_project";
    }

}
