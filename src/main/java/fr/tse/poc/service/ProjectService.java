package fr.tse.poc.service;

import fr.tse.poc.dao.ProjectRepository;
import fr.tse.poc.domain.Project;
import fr.tse.poc.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepo;

    /**
     * Return a project by its id
     * @param idProject The id of the project we're looking for
     * @return the project if it exists, else return null
     */
    public Project findProject(long idProject){
        return projectRepo.findById(idProject).orElse(null);
    }

    /**
     * Return all projects
     * @return All projects in the table Project
     */
    public List<Project> findAllProjects(){
        return this.projectRepo.findAll();
    }

    public Project createProject(Project project){
        return this.projectRepo.save(project);
    }

    public Project checkProjectExists(Long projectId){
        Project project = this.findProject(projectId);
        if (project == null) {
            throw new ResourceNotFoundException("Project not found with id = " + projectId);
        }
        return project;
    }
}
