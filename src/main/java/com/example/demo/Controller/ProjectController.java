package com.example.demo.Controller;

import com.example.demo.Model.Employee;
import com.example.demo.Model.Project;
import com.example.demo.Services.EmployeeService;
import com.example.demo.Services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("projects")
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    EmployeeService employeeService;
    @GetMapping
    public String getAllProjects(Model model) {
        List<Project> projects = projectService.getAllProject();
        model.addAttribute("projects", projects);
        return "projects";
    }
    @GetMapping("/new")
    public String showNewProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "projectForm";
    }
    @PostMapping("/new")
    public String createProject(@ModelAttribute("project") Project project) {
        projectService.createProject(project);
        return "redirect:/user";
    }
    @GetMapping("/update")
    public String updateProjectForm(Model model) {
        model.addAttribute("projectUpdate", new Project());
        return "updateProject";
    }
    @PostMapping("/update")
    public String updateProject(@ModelAttribute("projectUpdate") Project project) {
        projectService.updateProject(project);
        return "redirect:/projects";
    }
    @GetMapping("/addemployee/{id}")
    public String addEmployeeForm(Model model, @PathVariable String id) {
        List<Employee> employees = employeeService.findAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("projectId", id);
        return "addEmployee";
    }
    @PostMapping("/addemployee/{projectId}")
    public String addEmployee(@RequestParam("employeeId") String employeeId,@ModelAttribute("projectId") String projectId) {

        projectService.addEmployee(projectId, employeeId);
        return "redirect:/user";
    }
}
