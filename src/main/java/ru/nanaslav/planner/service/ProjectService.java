package ru.nanaslav.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nanaslav.planner.model.Account;
import ru.nanaslav.planner.model.Participant;
import ru.nanaslav.planner.model.Project;
import ru.nanaslav.planner.model.Role;
import ru.nanaslav.planner.repository.AccountRepository;
import ru.nanaslav.planner.repository.ParticipantRepository;
import ru.nanaslav.planner.repository.ProjectRepository;

import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ParticipantRepository participantRepository;

    public long createProject(String projectName,
                              String description,
                              Account account) {
        Project project = new Project(projectName, description, account);
        Participant participant = new Participant(account, project, Role.CREATOR);
        project.addParticipant(participant);
        projectRepository.save(project);
        participantRepository.save(participant);
        return project.getId();
    }

    public void addParticipant(long projectId, Account account) {
        Project project = projectRepository.findById(projectId).orElseThrow(IllegalStateException::new);
        Participant participant = new Participant(account, project, Role.PARTICIPANT);
        participantRepository.save(participant);
        project.addParticipant(participant);
        projectRepository.save(project);
    }

    public List<Project> getProjectsByAccount(Account account, boolean lazy) {
        List<Project> projects = new ArrayList<>();
        List<Participant> participants = participantRepository.findAllByParticipant(account);
        if (lazy && participants.size() > 6) {
            int count = 0;
            for (Participant participant : participants) {
                projects.add(participant.getProject());
                count++;
                if (count == 6) break;
            }
        } else {
            for (Participant participant : participants) {
                projects.add(participant.getProject());
            }
        }
        return projects;
    }

    public List<Project> getProjectsByAccount(Account account) {
        return getProjectsByAccount(account, false);
    }


}
