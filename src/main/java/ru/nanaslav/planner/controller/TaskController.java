package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nanaslav.planner.model.Project;
import ru.nanaslav.planner.model.Task;
import ru.nanaslav.planner.repository.ProjectRepository;
import ru.nanaslav.planner.repository.TaskRepository;

/**
 * Controller for task
 * Manage task information
 * @author NanaSlav
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

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

        Project project = projectRepository.findById(projectId).orElseThrow(IllegalStateException::new);
        Task task = new Task(taskName, description, project);
        task.setDone(false);
        taskRepository.save(task);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/add/{projectId}")
    public String showAddTask(@PathVariable long projectId, Model model) {
        Project project = projectRepository.findById(projectId).orElseThrow(IllegalStateException::new);
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
        Task task = taskRepository.findById(id).orElseThrow(IllegalStateException::new);
        task.setDone(true);
        return "";
    }

    @GetMapping("/task/{taskId}")
    public String showTaskPage(@PathVariable long taskId, Model model) {
        Task task = taskRepository.findById(taskId).orElseThrow(IllegalStateException::new);
        model.addAttribute("task", task);
        return "task/task-details";
    }

}
