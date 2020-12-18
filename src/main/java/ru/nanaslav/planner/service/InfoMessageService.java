package ru.nanaslav.planner.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class InfoMessageService {
    public void createErrorMessage(Model model, String title, String message) {
        model.addAttribute("color", "w3-red");
        createMessage(model, title, message);
    }

    public void createInfoMessage(Model model, String title, String message) {
        model.addAttribute("color", "w3-blue");
        createMessage(model, title, message);
    }

    public void createSuccessMessage(Model model, String title, String message) {
        model.addAttribute("color", "w3-green");
        createMessage(model, title, message);
    }

    private void createMessage(Model model, String title, String message) {
        model.addAttribute("hasMessage", true);
        model.addAttribute("title", title);
        model.addAttribute("message", message);
    }
}
