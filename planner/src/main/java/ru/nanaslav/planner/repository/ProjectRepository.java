package ru.nanaslav.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nanaslav.planner.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
