package com.example.demo.Model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
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

    public void addToSet(ObjectId employeeId) {
        this.employee_id.add(employeeId);
    }
}
