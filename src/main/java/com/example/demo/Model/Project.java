package com.example.demo.Model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "projects")
public class Project {
    @Id
    private String id;
    private String title;
    private String description;
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String startDate;
//    private Date endDate;
    private Set<ObjectId> employee_id;
    private List<ProjectUpdate> workUpdates;

    public void addToEmployeeSet(ObjectId employeeId) {
        this.employee_id.add(employeeId);
    }

    public void addToWorkUpdates(ProjectUpdate projectUpdate) {
        if (this.workUpdates == null) workUpdates = new ArrayList<ProjectUpdate>();
        this.workUpdates.add(projectUpdate);
    }


}
