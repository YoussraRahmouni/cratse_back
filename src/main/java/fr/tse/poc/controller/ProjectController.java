package fr.tse.poc.controller;

import fr.tse.poc.domain.Project;
import fr.tse.poc.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProjects(){
        List<Project> projects = new ArrayList<>(projectService.findAllProjects());
        if(projects.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping("/projects")
    @PreAuthorize("hasAnyAuthority('Manager')")
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project){
        Project newProject = this.projectService.createProject(project);
        return new ResponseEntity<>(newProject, HttpStatus.CREATED);
    }

}
