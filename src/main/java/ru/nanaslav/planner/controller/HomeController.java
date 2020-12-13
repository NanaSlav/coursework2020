package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.service.ProjectService;
import ru.nanaslav.planner.service.TaskService;

/**
 * Controller for home pages
 * Manage information that contains of home pages
 * @author NanaSlav
 */
@Controller
public class HomeController {
    @Autowired
    ProjectService projectService;
    @Autowired
    TaskService taskService;

    /**
     * Main page without authentication
     * @return view "home" or redirect to /home if user is authorized
     */
    @GetMapping("/")
    public String home(@AuthenticationPrincipal Account account, Model model) {
        if (account != null) {
            return "redirect:/home";
        }
        return "home";
    }

    /**
     * Main page for registered users
     * @return view "main"
     */
    @GetMapping("/home")
    public String mainPage(@AuthenticationPrincipal Account account, Model model)
    {
        model.addAttribute("projects", projectService.getProjectsByAccount(account, true));
        model.addAttribute("tasks", taskService.getTasksByAccount(account));
        return "main";
    }
}
