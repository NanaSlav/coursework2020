package ru.nanaslav.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nanaslav.planner.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
