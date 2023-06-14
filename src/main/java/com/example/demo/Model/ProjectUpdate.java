package com.example.demo.Model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document(collection = "updates")
@Data
public class ProjectUpdate {
    @Id
    private String id;

    private ObjectId projectId;
    private String updaterName;
    private String description;
    private String timeRange;
    private String dateOfUpdate;
}
