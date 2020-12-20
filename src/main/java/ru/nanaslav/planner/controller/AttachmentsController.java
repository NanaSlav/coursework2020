package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Attachment;
import ru.nanaslav.planner.service.AttachmentService;
import ru.nanaslav.planner.service.PaginationService;

import java.util.List;

@Controller
@RequestMapping("/attach")
public class AttachmentsController {
    @Autowired
    AttachmentService attachmentService;

    @Autowired
    PaginationService paginationService;

    @GetMapping("/")
    public String getAllAttachments(@AuthenticationPrincipal Account account, Model model,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "20") int size) {
        PageRequest request = PageRequest.of(page - 1, size);
        List<Attachment> attachmentList = attachmentService.getAttachmentsByAccount(account);

        int start = (int) request.getOffset();
        int end = Math.min((start + request.getPageSize()), attachmentList.size());
        Page<Attachment> attachments = new PageImpl<Attachment>(attachmentList.subList(start, end), request, attachmentList.size());
        int last = attachments.getTotalPages();
        paginationService.setPages(page, last, size, model);
        model.addAttribute("urlBegin", "/attach/?");
        model.addAttribute("attachments", attachments);
        return "attachments-list";
    }

}
