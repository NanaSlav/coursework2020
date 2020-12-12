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
import ru.nanaslav.planner.service.ProjectService;

import java.io.IOException;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    ProjectService projectService;

    private void accountInfo(Model model, Account account) {
        model.addAttribute("username", account.getUsername());
        model.addAttribute("email", account.getEmail());
        model.addAttribute("avatar", account.getAvatar());
        model.addAttribute("projects", projectService.getProjectsByAccount(account, true));
    }

    @GetMapping()
    public String showAccount(@AuthenticationPrincipal Account account, Model model) {
        accountInfo(model, account);
        return "/account/account";
    }

    @GetMapping("/edit")
    public String showAccountEdit(@AuthenticationPrincipal Account account, Model model) {
        accountInfo(model, account);
        return "account/edit-profile";
    }

    @PostMapping("/edit")
    public String accountEdit(@AuthenticationPrincipal Account account,
                              @RequestParam String username,
                              @RequestParam String email,
                              @RequestParam String bio) {
        if (accountService.editAccountInfo(account, username, email, bio)) {
            return "redirect:/account";
        } else {
            // TODO: error message
            return "redirect:/account/edit";
        }

    }

    @PostMapping("/edit/avatar")
    public String editAvatar(@AuthenticationPrincipal Account account,
                             @RequestParam MultipartFile avatar) throws IOException {
        accountService.setAvatar(account, avatar);
        return "redirect:/account/edit";
    }

    @PostMapping("/edit/password")
    public String changePassword(@AuthenticationPrincipal Account account,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword) {
        accountService.changePassword(account, currentPassword, newPassword);
        return "redirect:/account";
    }
}
