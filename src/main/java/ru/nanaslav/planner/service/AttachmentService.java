package ru.nanaslav.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Attachment;
import ru.nanaslav.planner.model.Task;
import ru.nanaslav.planner.repository.AttachmentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttachmentService {
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    TaskService taskService;

    public List<Attachment> getAttachmentsByAccount(Account account) {
        List<Task> tasks = taskService.getTasksByAccount(account);
        List<Attachment> attachments = new ArrayList<>();
        for (Task task : tasks) {
            attachments.addAll(task.getAttachments());
        }
        return attachments;
    }

    public List<Attachment> search(String q, Account account) {
        List<Attachment> allAttachments = getAttachmentsByAccount(account);
        return search(q, allAttachments);
    }

    private List<Attachment> search(String q, List<Attachment> allAttachments) {
        List<Attachment> attachments = new ArrayList<>();
        for (Attachment attachment : allAttachments) {
            if (attachment.getName().contains(q)) {
                attachments.add(attachment);
            }
        }
        return attachments;
    }
}
