package fr.tse.poc.dao;

import fr.tse.poc.domain.Imputation;
import fr.tse.poc.domain.ImputationOnlyProject;
import fr.tse.poc.domain.Project;
import fr.tse.poc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ImputationRepository extends JpaRepository<Imputation, Long> {

    List<Imputation> findByUserAndProject(User user, Project project);
    List<Imputation> findByUser(User user);
    Set<ImputationOnlyProject> findDistinctProjectByUser(User user);

}
