package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.repository.AccountRepository;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping()
    public String showAccount(@AuthenticationPrincipal Account account, Model model) {
        model.addAttribute("username", account.getUsername());
        // model.addAttribute("email", account.getEmail());
        return "account";
    }

}
