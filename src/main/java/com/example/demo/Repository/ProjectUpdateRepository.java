package com.example.demo.Repository;

import com.example.demo.Model.ProjectUpdate;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectUpdateRepository extends MongoRepository<ProjectUpdate, String> {
    List<ProjectUpdate> findAllByProjectId(ObjectId objectId);
}
