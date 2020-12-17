package ru.nanaslav.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Project;
import ru.nanaslav.planner.model.Task;
import ru.nanaslav.planner.repository.ProjectRepository;
import ru.nanaslav.planner.repository.TaskRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectService projectService;

    public void createTask(String taskName, String description, long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(IllegalStateException::new);
        Task task = new Task(taskName, description, project);
        task.setDone(false);
        taskRepository.save(task);
    }
    public void setDone(long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(IllegalStateException::new);
        task.setDone(true);
        taskRepository.save(task);
    }

    public List<Task> getTasksByAccount(Account account) {
        List<Project> projects = projectService.getProjectsByAccount(account);
        List<Task> tasks = new ArrayList<>();
        for (Project project : projects) {
            for (Task task : project.getTasks()) {
                if (!task.isDone()) {
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }

    public Task getTaskById(long id) {
        return taskRepository.findById(id).orElseThrow(IllegalStateException::new);
    }


    public boolean delete(long id) {
        if (taskRepository.findById(id).isPresent()) {
            taskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public void editTask(long id, String name, String description) {
        Task task = taskRepository.findById(id).orElseThrow(IllegalStateException::new);
        task.setName(name);
        task.setDescription(description);
        taskRepository.save(task);
    }


    public List<Task> search(String q, Account account) {
        return search(q, getTasksByAccount(account));
    }

    public List<Task> search(String q, Project project) {
        return search(q, project.getTasks());
    }

    public List<Task> search(String q) {
        return search(q, taskRepository.findAll());
    }

    private List<Task> search(String q, List<Task> allTasks) {
        LinkedList<Task> tasks = new LinkedList<>();
        for (Task task: allTasks) {
            if (task.getName().contains(q)) {
                tasks.addFirst(task);
            } else {
                if (task.getDescription().contains(q)) {
                    tasks.addLast(task);
                }
            }
        }
        return tasks;
    }
}
