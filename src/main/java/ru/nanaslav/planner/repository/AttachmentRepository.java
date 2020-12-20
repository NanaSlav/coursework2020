package ru.nanaslav.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nanaslav.planner.model.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {}