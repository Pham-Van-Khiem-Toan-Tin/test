package com.example.demo.Services;

import com.example.demo.Config.JWTuntil;
import com.example.demo.Model.Employee;

import com.example.demo.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements UserDetailsService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public void register(Employee employee) {
        if(employeeRepository.findByName(employee.getName()) != null) {
            throw new RuntimeException("Username already exists");
        }
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        employeeRepository.save(employee);
    }
    public List<Employee> findAllEmployees() {return employeeRepository.findAll();}
    public Employee findByEmail(String email) { return employeeRepository.findByEmail(email); }
    public Employee findByName(String username) {
        return employeeRepository.findByName(username);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email);
        if (employee == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(employee.getEmail(), employee.getPassword(), employee.getAuthorities());
    }
}
