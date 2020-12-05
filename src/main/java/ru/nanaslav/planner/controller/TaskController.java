package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
     * @param title
     * @param description
     * @param project_id project that contains this task
     * @return view name
     */
    @PostMapping("/add")
    public String addTask(@RequestParam String title,
                          @RequestParam String description,
                          @RequestParam long project_id) {

        Project project = projectRepository.findById(project_id).orElseThrow(IllegalStateException::new);
        Task task = new Task(title, description, project);
        task.setDone(false);
        taskRepository.save(task);
        // TODO: add view - tasks
        return "tasks";
    }

    @GetMapping("/add/{projectId}")
    public String showAddTask(@PathVariable long projectId) {
        // TODO: add view - add task
        return "add_task";
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

}
