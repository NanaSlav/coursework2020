package ru.nanaslav.planner.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.nanaslav.planner.model.Account;

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
    public String home(@AuthenticationPrincipal Account account) {
        if (account != null) {
            return "redirect:/home";
        }
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
