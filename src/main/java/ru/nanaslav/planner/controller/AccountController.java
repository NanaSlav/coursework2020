package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.repository.AccountRepository;
import ru.nanaslav.planner.service.AccountService;

import java.io.IOException;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @GetMapping()
    public String showAccount(@AuthenticationPrincipal Account account, Model model) {
        model.addAttribute("username", account.getUsername());
        // model.addAttribute("email", account.getEmail());
        model.addAttribute("avatar", account.getAvatar());
        model.addAttribute("uploadDir", System.getProperty("user.dir") + "/avatars/");
        return "account";
    }

    @PostMapping("/edit")
    public String accountEdit(@RequestParam MultipartFile avatar,
                              @AuthenticationPrincipal Account account) throws IOException {
        accountService.setAvatar(account, avatar);
        return "redirect:/account";
    }

}
