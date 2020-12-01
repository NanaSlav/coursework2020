package ru.nanaslav.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.repository.AccountRepository;

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
    @PostMapping("/registration")
    public String addUser(@RequestParam String username,
                          @RequestParam String email,
                          @RequestParam String password) {

        if (accountRepository.findByUsername(username) != null) {
            // TODO: error username exists
            return "redirect:/";
        }
        if (accountRepository.findByEmail(email) != null) {
            // TODO: error email exists
            return "redirect:/";
        }

        Account account = new Account();
        account.setUsername(username);
        account.setEmail(email);
        account.setPassword(bCryptPasswordEncoder.encode(password));
        accountRepository.save(account);

        return "redirect:/home";
    }

}
