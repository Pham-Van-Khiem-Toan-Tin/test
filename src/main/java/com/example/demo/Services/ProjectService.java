package com.example.demo.Services;

import com.example.demo.Model.Project;
import com.example.demo.Repository.ProjectRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    public List<Project> getAllProject() {
        return projectRepository
                .findAll();
    }
    public Project createProject(Project project) {
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
    public void deleteProject(String id) {
        projectRepository.deleteById(id);
    }
}
