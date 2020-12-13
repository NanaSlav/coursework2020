package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nanaslav.planner.service.ProjectService;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    ProjectService projectService;

    @GetMapping("/")
    public String search(@RequestParam String q, Model model) {
        model.addAttribute("q", q);
        model.addAttribute("projects", projectService.search(q));
        return "search-results";
    }
}
