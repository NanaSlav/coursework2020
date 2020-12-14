package ru.nanaslav.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${upload.path}")
    String uploadPath;


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
        account.setBio("My name is " + username);
        accountRepository.save(account);
        return account;
    }

    public void setAvatar(Account account,
                          MultipartFile avatar) throws IOException {
        String filename = "";
        if (avatar != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            filename = UUID.randomUUID().toString() + avatar.getOriginalFilename();
            avatar.transferTo(new File(uploadPath + "/" + filename));
        }
        account.setAvatar(filename);
        accountRepository.save(account);

    }

    public boolean editAccountInfo(Account account, String username, String email, String bio) {
        if (!account.getUsername().equals(username)) {
            if (accountRepository.findByUsername(username) == null) {
                account.setUsername(username);
            } else {
                return false;
            }
        }

        if (!account.getEmail().equals(email)) {
            if (accountRepository.findByEmail(email) == null) {
                account.setEmail(email);
            } else {
                return false;
            }
        }
        account.setBio(bio);
        accountRepository.save(account);
        return true;
    }

    public boolean changePassword(Account account, String currentPassword, String newPassword) {
        if (bCryptPasswordEncoder.matches(currentPassword, account.getPassword())) {
            account.setPassword(bCryptPasswordEncoder.encode(newPassword));
        } else {
            return false;
        }
        accountRepository.save(account);
        return true;
    }

    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public List<Account> search(String q) {
        List<Account> accounts = new ArrayList<>();
        for(Account account : accountRepository.findAll()) {
            if (account.getUsername().contains(q)) {
                accounts.add(account);
            }
        }
        return accounts;
    }
}