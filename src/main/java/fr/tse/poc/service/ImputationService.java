package fr.tse.poc.service;

import fr.tse.poc.dao.ImputationRepository;
import fr.tse.poc.domain.Imputation;
import fr.tse.poc.domain.ImputationOnlyProject;
import fr.tse.poc.domain.Project;
import fr.tse.poc.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ImputationService {

    @Autowired
    ImputationRepository imputationRepo;

    public List<Imputation> findProjectImputations(User user, Project project){
        return this.imputationRepo.findByUserAndProject(user, project);
    }

    public List<Imputation> findAllImputations(User user){
        return this.imputationRepo.findByUser(user);
    }

    public Set<ImputationOnlyProject> findProjectsByUser(User user){
        return this.imputationRepo.findDistinctProjectByUser(user);
    }

    public Imputation createImputation(Imputation imputation){
        return this.imputationRepo.save(imputation);
    }

}
