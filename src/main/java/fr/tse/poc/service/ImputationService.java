package fr.tse.poc.service;

import fr.tse.poc.dao.ImputationRepository;
import fr.tse.poc.domain.Imputation;
import fr.tse.poc.domain.ImputationOnlyProject;
import fr.tse.poc.domain.Project;
import fr.tse.poc.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public List<Project> findProjectsByUser(User user){
        List<ImputationOnlyProject> listImputationOnlyProject = this.imputationRepo.findDistinctProjectByUser(user);
        List<Project> listProject = new ArrayList<>();
        listImputationOnlyProject.forEach(item -> listProject.add(item.getProject()));
        return listProject;
    }

    public Imputation createImputation(Imputation imputation){
        return this.imputationRepo.save(imputation);
    }

    public Imputation findImputationByUserAndProjectAndDate(User user, Project project, LocalDate dateImputation){
        return this.imputationRepo.findByUserAndProjectAndDateImputation(user, project, dateImputation).orElse(null);
    }

    public List<Imputation> findImputationByUserInMonthAndYear(User user, Integer month, Integer year){
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return this.imputationRepo.findByUserAndDateImputationBetween(user, startDate, endDate);
    }

}
