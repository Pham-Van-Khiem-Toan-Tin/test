package com.example.demo.Controller;

import com.example.demo.Model.Employee;
import com.example.demo.Model.Project;
import com.example.demo.Model.ProjectUpdate;
import com.example.demo.Services.EmployeeService;
import com.example.demo.Services.ProjectService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        return "updateProje" +
                "ct";
    }
    @PostMapping("/update")
    public String updateProject(@ModelAttribute("projectUpdate") Project project) {
        projectService.updateProject(project);
        return "redirect:/projects";
    }
    @GetMapping("/addemployee/{projectId}")
    public String addEmployeeForm(Model model, @PathVariable String projectId) {
        List<Employee> employees = employeeService.findAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("projectId", projectId);
        return "addEmployee";
    }
    @PostMapping("/addemployee/{projectId}")
    public String addEmployee(@RequestParam("employeeId") String employeeId,@ModelAttribute("projectId") String projectId) {
        projectService.addEmployee(projectId, employeeId);
        return "redirect:/projects";
    }

    @GetMapping("/workupdate/{projectId}")
    public String serveUpdateForm(Model model, @PathVariable("projectId") String projectId) {
        model.addAttribute("projectUpdate", new ProjectUpdate());
        model.addAttribute("projectId", projectId);
        Project project = projectService.findByProjectId(projectId);
        String minTimeUpdate = project.getStartDate();
        model.addAttribute("minTimeUpdate", minTimeUpdate);
        return "workUpdate";
    }

    @PostMapping("/workupdate/{projectId}")
    public String addUpdateToProject(@ModelAttribute("projectUpdate") ProjectUpdate projectUpdate,
                                     @PathVariable("projectId") String projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ObjectId objectId = new ObjectId(projectId);
        projectUpdate.setProjectId(objectId);
        projectUpdate.setTimeRange(projectUpdate.getTimeRange().replace("to", "-"));
        projectUpdate.setDateOfUpdate(getCurrentDate());
        projectUpdate.setUpdaterName(employeeService.findByEmail(authentication.getName()).getName());
        System.out.println(projectUpdate);
        projectService.addWorkUpdates(projectUpdate);
        return "redirect:/projects";
    }

    @GetMapping("/workupdate/viewByMonth")
    public String viewByMonth(Model model) {
        List<Project> foundProjects = projectService.getAllProjectsUpdatedByMonth();
        model.addAttribute("projects", foundProjects);
        return "workUpdateDetails";
    }

    private String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDate.format(formatter);

    }
}
