package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Project;
import ru.nanaslav.planner.service.AccountService;
import ru.nanaslav.planner.service.PaginationService;
import ru.nanaslav.planner.service.ProjectService;
import ru.nanaslav.planner.service.TaskService;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    ProjectService projectService;
    @Autowired
    TaskService taskService;
    @Autowired
    AccountService accountService;
    @Autowired
    PaginationService paginationService;

    @GetMapping("/")
    public String search(@RequestParam String q,
                         @RequestParam(value = "type", defaultValue = "project") String type,
                         @RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int size,
                         Model model) {
        PageRequest request = PageRequest.of(page - 1, size);
        int start = (int) request.getOffset();

        model.addAttribute("q", q);
        int end = 0;
        int last = 0;
        switch (type) {
            case "account":
                List<Account> accountList = accountService.search(q);
                end = Math.min(start + request.getPageSize(), accountList.size());
                Page<Account> accounts = new PageImpl<Account>(accountList.subList(start, end), request, accountList.size());
                last = accounts.getTotalPages();
                model.addAttribute("results", accounts);
                model.addAttribute("type", "account");
                break;
            case "project":
            default:
                List<Project> projectList = projectService.search(q);
                end = Math.min(start + request.getPageSize(), projectList.size());
                Page<Project> projects = new PageImpl<Project>(projectList.subList(start, end), request, projectList.size());
                last = projects.getTotalPages();
                model.addAttribute("results", projects);
                model.addAttribute("type", "project");
        }

        paginationService.setPages(page, last, size, model);
        model.addAttribute("urlBegin", "/search/?q=" + q + "&");

        return "search-results";
    }
}
