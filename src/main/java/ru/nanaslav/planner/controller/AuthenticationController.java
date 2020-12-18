package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.repository.AccountRepository;
import ru.nanaslav.planner.service.AccountService;
import ru.nanaslav.planner.service.InfoMessageService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

/**
 * Controller for account
 * Manage all account information
 * @author NanaSlav
 */
@Controller
public class AuthenticationController {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AccountService accountService;
    @Autowired
    InfoMessageService messageService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() { return "logout_temp"; }

    /**
     * create new user
     * @param username username should be unique
     * @param email
     * @param password
     * @return redirect to main page
     */

    // TODO: remove logic to account service

    @PostMapping("/registration")
    public String addUser(@RequestParam String username,
                          @RequestParam String email,
                          @RequestParam String password,
                          HttpServletRequest request,
                          Model model) throws IOException {

        if (accountRepository.findByUsername(username) != null) {
            messageService.createErrorMessage(model,"User with such username exists",
                    "Account with username '" + username + "' is exists. Please choose other username.");
            return "home";
        }
        if (accountRepository.findByEmail(email) != null) {
            messageService.createErrorMessage(model, "User with such email exists",
                    "Account with such email '" + email + "' exists. May be you already have account or make a mistake.");
            return "home";
        }
        Account account = accountService.createAccount(username, email, password);
        try {
            request.login(username, password);
        } catch (ServletException e) {
            LOGGER.error("Error while login ", e);
        }
        return "redirect:/account";
    }

}
