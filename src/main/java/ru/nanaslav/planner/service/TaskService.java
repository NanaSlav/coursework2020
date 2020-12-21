package ru.nanaslav.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nanaslav.planner.model.*;
import ru.nanaslav.planner.repository.AttachmentRepository;
import ru.nanaslav.planner.repository.CommentRepository;
import ru.nanaslav.planner.repository.ProjectRepository;
import ru.nanaslav.planner.repository.TaskRepository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    CommentRepository commentRepository;

    @Value("${upload.path}")
    String uploadPath;

    public Task createTask(String taskName, String description, long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(IllegalStateException::new);
        Task task = new Task(taskName, description, project);
        task.setDone(false);
        taskRepository.save(task);
        return task;
    }

    public Task createTask(String taskName, String description, long projectId, List<MultipartFile> files) throws IOException {
        Task task = createTask(taskName, description, projectId);
        List<Attachment> attachments = new ArrayList<>();
        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!Objects.equals(file.getOriginalFilename(), "")) {
                    Attachment attachment = new Attachment();
                    File uploadDir = new File(uploadPath + "/files/");
                    if (!uploadDir.exists()) {
                        uploadDir.mkdir();
                    }
                    String filename = UUID.randomUUID().toString() + file.getOriginalFilename();
                    file.transferTo(new File(uploadPath + "/files/" + filename));
                    attachment.setName(filename);
                    attachment.setTask(task);
                    attachmentRepository.save(attachment);
                    attachments.add(attachment);
                }
            }
            task.setAttachments(attachments);
            taskRepository.save(task);
        }
        return task;
    }

    public void setDone(long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(IllegalStateException::new);
        task.setDone(true);
        taskRepository.save(task);
    }

    public List<Task> getTasksByAccount(Account account) {
        List<Project> projects = projectService.getProjectsByAccount(account);
        LinkedList<Task> tasks = new LinkedList<>();
        Collections.reverse(projects);
        for (Project project : projects) {
            for (Task task : project.getTasks()) {
                if (!task.isDone()) {
                    tasks.addFirst(task);
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

    public void addComment(Task task, String text, Account author) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setTask(task);
        comment.setText(text);
        commentRepository.save(comment);
        List<Comment> comments = task.getComments();
        comments.add(comment);
        task.setComments(comments);
        taskRepository.save(task);
    }
}
