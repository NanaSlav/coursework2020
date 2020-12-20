package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Project;
import ru.nanaslav.planner.model.Task;
import ru.nanaslav.planner.repository.CommentRepository;
import ru.nanaslav.planner.repository.ProjectRepository;
import ru.nanaslav.planner.repository.TaskRepository;
import ru.nanaslav.planner.service.PaginationService;
import ru.nanaslav.planner.service.ProjectService;
import ru.nanaslav.planner.service.TaskService;

import java.io.IOException;
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
        model.addAttribute("urlBegin", "/tasks/?");
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
                          @RequestParam long projectId,
                          @RequestParam List<MultipartFile> files) throws IOException {

        taskService.createTask(taskName, description, projectId, files);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/add/{projectId}")
    public String showAddTask(@PathVariable long projectId, Model model,
                              @AuthenticationPrincipal Account account) {
        Project project = projectService.getProjectById(projectId);
        if (projectService.getAccess(project, account).equals("full")) {
            model.addAttribute("projectName", project.getName());
            return "task/add-task";
        } else {
            return "redirect:/projects/" + projectId;
        }

    }

    /**
     * Change task status to done
     * @param id task id
     * @return view name
     */
    @PostMapping("/{id}/done")
    public String setDone(@PathVariable long id, @AuthenticationPrincipal Account account) {
        String access = projectService.getAccess(taskService.getTaskById(id).getProject(), account);
        if (access.equals("full")) {
            taskService.setDone(id);
            return "redirect:/tasks/task/" + id;
        } else {
            return "redirect:/tasks";
        }
    }

    @GetMapping("/task/{taskId}")
    public String showTaskPage(@PathVariable long taskId, Model model, @AuthenticationPrincipal Account account) {
        Task task = taskService.getTaskById(taskId);
        String access = projectService.getAccess(task.getProject(), account);
        if (!access.equals("guest")) {
            model.addAttribute("task", task);
            return "task/task-details";
        } else {
            return "redirect:/tasks/";
        }
    }

    @PostMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable long taskId) {
        taskService.delete(taskId);
        return "redirect:/tasks/";
    }

    @GetMapping("/edit/{taskId}")
    public String showEditForm(@PathVariable long taskId, Model model,
                               @AuthenticationPrincipal Account account) {
        Task task = taskService.getTaskById(taskId);
        String access = projectService.getAccess(task.getProject(), account);
        if (access.equals("full")) {
            model.addAttribute("taskName", task.getName());
            model.addAttribute("taskDescription", task.getDescription());
            model.addAttribute("projectName", task.getProject().getName());
            model.addAttribute("projectId", task.getProject().getId());
            return "task/edit-task";
        } else {
            return "redirect:/tasks/task/" + taskId;
        }
    }

    @PostMapping("/edit/{taskId}")
    public String editTask(@PathVariable long taskId,
                           @RequestParam String name,
                           @RequestParam String description) {
        taskService.editTask(taskId, name, description);
        return "redirect:/tasks/task/" + taskId;
    }

    @PostMapping("{taskId}/comment/")
    public String addComment(@RequestParam String text,
                             @AuthenticationPrincipal Account account,
                             @PathVariable long taskId) {
        Task task = taskService.getTaskById(taskId);
        taskService.addComment(task, text, account);
        return "redirect:/tasks/task/" + taskId;
    }
}
