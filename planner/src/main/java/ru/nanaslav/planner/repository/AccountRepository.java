package ru.nanaslav.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nanaslav.planner.model.Account;

public interface AccountRepository  extends JpaRepository<Account, Long> {}
