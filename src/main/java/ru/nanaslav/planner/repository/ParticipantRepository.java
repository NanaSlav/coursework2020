package ru.nanaslav.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Participant findByParticipant(Account account);
}
