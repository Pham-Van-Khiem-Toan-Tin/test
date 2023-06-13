package com.example.demo.Model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@Document(collection = "employees")
@Data
public class Employee {
    @Id
    private String id;
    private String employeeId;
    private String name;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Set<ObjectId> project_id;
}
