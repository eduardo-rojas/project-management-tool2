package me.carlosehr.ppmtool.services;

import me.carlosehr.ppmtool.domain.Backlog;
import me.carlosehr.ppmtool.domain.User;
import me.carlosehr.ppmtool.exceptions.ProjectNotFoundException;
import me.carlosehr.ppmtool.repositories.BacklogRepository;
import me.carlosehr.ppmtool.repositories.ProjectRepository;
import me.carlosehr.ppmtool.domain.Project;
import me.carlosehr.ppmtool.exceptions.ProjectIdException;
import me.carlosehr.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;



    public Project saveOrUpdateProject(Project project, String username){

        //project.getId != null
        //find by db id -> null

        if(project.getId() != null){
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

            if(existingProject != null && (!existingProject.getProjectLeader().equals(username))){
                throw new ProjectNotFoundException("Project not found in your account");
            }else if(existingProject == null){
                throw new ProjectNotFoundException("Project with ID: '" + project.getProjectIdentifier()+
                        "' cannot be updated because it doesn't exist");
            }
        }

        try{

            User user = userRepository.findByUsername(username);

            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        }catch(Exception e){
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase()+ "' already exists");
        }
    }


    public Project findProjectByIdentifier(String projectId, String username){

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null){
            throw new ProjectIdException("Project ID '"+projectId+"' does not exist");

        }

        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }


        return project;
    }

    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectid, String username){


        projectRepository.delete(findProjectByIdentifier(projectid, username));
    }


}