package ru.nanaslav.planner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for home pages
 * Manage information that contains of home pages
 * @author NanaSlav
 */
@Controller
public class HomeController {
    /**
     * Main page without authentication
     * @return view "home"
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * Main paga for registered users
     * @return view "main"
     */
    @GetMapping("/home")
    public String mainPage() {
        return "main";
    }
}
