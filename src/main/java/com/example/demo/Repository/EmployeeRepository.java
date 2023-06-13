package com.example.demo.Repository;

import com.example.demo.Model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Employee findByName(String username);
    Employee findByEmail(String email);
}
