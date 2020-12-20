package ru.nanaslav.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nanaslav.planner.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
