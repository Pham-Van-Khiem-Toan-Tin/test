package com.example.demo.Services;

import com.example.demo.Model.ProjectUpdate;
import com.example.demo.Repository.ProjectUpdateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectUpdateService {
    @Autowired
    ProjectUpdateRepository projectUpdateRepository;
    @Autowired
    EmployeeService employeeService;

    public void addToProject(ProjectUpdate projectUpdate) {
        projectUpdateRepository.save(projectUpdate);
    }
}
