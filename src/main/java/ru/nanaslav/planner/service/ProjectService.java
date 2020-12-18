package ru.nanaslav.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
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
import java.util.LinkedList;
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
        LinkedList<Project> projects = new LinkedList<>();
        List<Participant> participants = participantRepository.findAllByParticipant(account);
        if (lazy && participants.size() > 6) {
            int count = 0;
            for (Participant participant : participants) {
                projects.addFirst(participant.getProject());
                count++;
                if (count == 6) break;
            }
        } else {
            for (Participant participant : participants) {
                projects.addFirst(participant.getProject());
            }
        }
        return projects;
    }

    public List<Project> getProjectsByAccount(Account account) {
        return getProjectsByAccount(account, false);
    }

    public List<Project> search(String q) {
        List<Project> allProjects = projectRepository.findAll();
        return search(q, allProjects);
    }

    public List<Project> search(String q, Account account) {
        List<Project> accountProjects = getProjectsByAccount(account);
        return search(q, accountProjects);
    }

    private List<Project> search(String q, List<Project> projects) {
        LinkedList<Project> result = new LinkedList<>();
        for (Project project : projects) {
            if (project.getName().contains(q)) {
                result.addFirst(project);
            } else {
                if (project.getDescription().contains(q)) {
                    result.addLast(project);
                }
            }
        }
        return result;
    }

    public Project getProjectById(long id) {
        return projectRepository.findById(id).orElseThrow(IllegalStateException::new);
    }

    public void editProject(long id, String name, String description) {
        Project project = projectRepository.findById(id).orElseThrow(IllegalStateException::new);
        project.setName(name);
        project.setDescription(description);
        projectRepository.save(project);
    }

    public boolean delete(long id) {
        if (projectRepository.findById(id).isPresent()) {
            projectRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public String getAccess(Project project, Account account) {
        for (Participant participant : project.getParticipants()) {
            if (participant.getParticipant().getUsername().equals(account.getUsername())) {
                return "full";
            }
        }
        return "guest";
    }

    public String getAccess(long projectId, Account account) {
        Project project = getProjectById(projectId);
        return getAccess(project, account);
    }

}
