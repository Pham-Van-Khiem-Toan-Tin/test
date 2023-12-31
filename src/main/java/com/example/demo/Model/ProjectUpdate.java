package com.example.demo.Model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document
@Data
public class ProjectUpdate {
    private ObjectId projectId;
    private String updaterName;
    private String description;
    private String timeRange;
    private String dateOfUpdate;

    public int returnUpdateMonth() {
        String[] splittedDate = dateOfUpdate.split("/");
        return Integer.parseInt(splittedDate[1]);
    }

    @Override
    public String toString() {
        return "[" + timeRange + "] " + description;
    }
}
