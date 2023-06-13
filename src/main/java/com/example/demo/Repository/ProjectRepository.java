package com.example.demo.Repository;

import com.example.demo.Model.Project;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> findAll();
    Project save(Project project);

}
