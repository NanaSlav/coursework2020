package ru.nanaslav.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.repository.AccountRepository;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public static final String uploadingDir = System.getProperty("user.dir") + "/src/main/resources/static/img/avatar";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails account = accountRepository.findByUsername(username);
        if (account == null) {
            return accountRepository.findByEmail(username);
        }
        return account;
    }

    public Account createAccount(String username,
                              String email,
                              String password) throws IOException {
        Account account = new Account();
        account.setUsername(username);
        account.setEmail(email);
        account.setPassword(bCryptPasswordEncoder.encode(password));
        accountRepository.save(account);
        return account;
    }

    public void setAvatar(Account account,
                          MultipartFile avatar) throws IOException {
        String filename = "";
        if (avatar != null) {
            File uploadDir = new File(uploadingDir);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            filename = UUID.randomUUID().toString() + avatar.getOriginalFilename();
            avatar.transferTo(new File(uploadingDir + "/" + filename));
        }
        account.setAvatar(filename);
        accountRepository.save(account);

    }
}