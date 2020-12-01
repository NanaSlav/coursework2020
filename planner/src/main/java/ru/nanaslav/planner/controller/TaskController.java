package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.nanaslav.planner.model.Project;
import ru.nanaslav.planner.model.Task;
import ru.nanaslav.planner.repository.ProjectRepository;
import ru.nanaslav.planner.repository.TaskRepository;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

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

    @GetMapping("/add")
    public String showAddTask() {
        // TODO: add view - add task
        return "add_task";
    }

    @PostMapping("/{id}/done")
    public String setDone(@PathVariable long id) {
        Task task = taskRepository.findById(id).orElseThrow(IllegalStateException::new);
        task.setDone(true);
        return "";
    }

}
