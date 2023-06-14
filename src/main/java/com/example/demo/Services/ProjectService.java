package com.example.demo.Services;

import com.example.demo.Model.Project;
import com.example.demo.Model.ProjectUpdate;
import com.example.demo.Repository.ProjectRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    public List<Project> getAllProject() {
        return projectRepository
                .findAll();
    }
    public Project findByProjectId(String projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }
    public Project createProject(Project project) {
        String startDate = project.getStartDate();
        String[] dateArr = startDate.split("-");
        String newStartDateFormat = dateArr[2] + "-" + dateArr[1] + "-" + dateArr[0];
        project.setStartDate(newStartDateFormat);
        return projectRepository.save(project);
    }
    public void updateProject(Project project) {
        String id = project.getId();
        System.out.println("id: " + id);
        Project project1 = projectRepository.findById(id).orElse(null);
        if(project1 != null ) {
            project1.setTitle(project.getTitle());
            project1.setDescription(project.getDescription());
        }
        projectRepository.save(project);
    }

    public void addEmployeeToProject(String projectId, String employeeId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        ObjectId objectId = new ObjectId(employeeId);
        project.addToEmployeeSet(objectId);
        projectRepository.save(project);
    }

    public void addWorkUpdates(ProjectUpdate projectUpdate) {
        Project update = projectRepository.findById(projectUpdate.getProjectId());
        update.addToWorkUpdates(projectUpdate);
        projectRepository.save(update);
    }

    public void addEmployee(String projectId, String employeeId) {
       Project project = projectRepository.findById(projectId).orElse(null);

       if(project==null) return;

       ObjectId objectId = new ObjectId(employeeId);
       if(project.getEmployee_id() != null) {
           project.getEmployee_id().add(objectId);
       } else {
           project.setEmployee_id(new HashSet<ObjectId>());
           project.getEmployee_id().add(objectId);
       }
        projectRepository.save(project);

    }

    public List<Project> getAllProjectsUpdatedByMonth() {
        LocalDate currDate = LocalDate.now();
        int currMonth = currDate.getMonthValue();

        List<Project> allProjects = projectRepository.findAll();
        List<Project> foundProjects = new ArrayList<>();
        for (int i = 0; i < allProjects.size(); ++i) {
            Project currentProject = allProjects.get(i);
            if (currentProject.getWorkUpdates() == null) continue;
            for (int j = 0; j < currentProject.getWorkUpdates().size(); ++j) {
                ProjectUpdate currProjectupdate = currentProject.getWorkUpdates().get(j);
                int updateMonth = currProjectupdate.returnUpdateMonth();
                if (updateMonth == currMonth) {
                    foundProjects.add(currentProject);
                    break;
                }
            }
        }
        return foundProjects;
    }
    public void deleteProject(String id) {
        projectRepository.deleteById(id);
    }
}
