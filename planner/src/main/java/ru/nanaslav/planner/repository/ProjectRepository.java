package ru.nanaslav.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByNameAndCreator(String name, Account creator);
}
