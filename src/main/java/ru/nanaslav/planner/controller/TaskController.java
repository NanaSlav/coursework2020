package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Project;
import ru.nanaslav.planner.model.Task;
import ru.nanaslav.planner.repository.ProjectRepository;
import ru.nanaslav.planner.repository.TaskRepository;
import ru.nanaslav.planner.service.PaginationService;
import ru.nanaslav.planner.service.ProjectService;
import ru.nanaslav.planner.service.TaskService;

import java.util.List;

/**
 * Controller for task
 * Manage task information
 * @author NanaSlav
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    ProjectService projectService;
    @Autowired
    PaginationService paginationService;

    @GetMapping("/")
    public String showTasks(@AuthenticationPrincipal Account account, Model model,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "20") int size) {
        PageRequest request = PageRequest.of(page - 1, size);
        List<Task> taskList = taskService.getTasksByAccount(account);

        int start = (int) request.getOffset();
        int end = Math.min((start + request.getPageSize()), taskList.size());
        Page<Task> tasks = new PageImpl<Task>(taskList.subList(start, end),request, taskList.size() );
        int last = tasks.getTotalPages();
        paginationService.setPages(page,last,size,model);
        model.addAttribute("urlBegin", "/tasks/");
        model.addAttribute("tasks", tasks);
        return "task/tasks-list";
    }

    /**
     * Create new task
     * @param taskName
     * @param description
     * @param projectId project that contains this task
     * @return view name
     */
    @PostMapping("/add")
    public String addTask(@RequestParam String taskName,
                          @RequestParam String description,
                          @RequestParam long projectId) {

        taskService.createTask(taskName, description, projectId);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/add/{projectId}")
    public String showAddTask(@PathVariable long projectId, Model model) {
        Project project = projectService.getProjectById(projectId);
        model.addAttribute("projectName", project.getName());
        return "task/add-task";
    }

    /**
     * Change task status to done
     * @param id task id
     * @return view name
     */
    @PostMapping("/{id}/done")
    public String setDone(@PathVariable long id) {
        taskService.setDone(id);
        return "redirect:/tasks/task/" + id;
    }

    @GetMapping("/task/{taskId}")
    public String showTaskPage(@PathVariable long taskId, Model model) {
        Task task = taskService.getTaskById(taskId);
        model.addAttribute("task", task);
        return "task/task-details";
    }

    @PostMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable long taskId) {
        taskService.delete(taskId);
        return "redirect:/tasks/";
    }

    @GetMapping("/edit/{taskId}")
    public String showEditForm(@PathVariable long taskId, Model model) {
        Task task = taskService.getTaskById(taskId);
        model.addAttribute("taskName", task.getName());
        model.addAttribute("taskDescription", task.getDescription());
        model.addAttribute("projectName", task.getProject().getName());
        model.addAttribute("projectId", task.getProject().getId());
        return "task/edit-task";
    }

    @PostMapping("/edit/{taskId}")
    public String editTask(@PathVariable long taskId,
                           @RequestParam String name,
                           @RequestParam String description) {
        taskService.editTask(taskId, name, description);
        return "redirect:/tasks/task/" + taskId;
    }
}
