package fr.tse.poc.dao;

import fr.tse.poc.domain.Imputation;
import fr.tse.poc.domain.Project;
import fr.tse.poc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImputationRepository extends JpaRepository<Imputation, Long> {

    List<Imputation> findByUserAndProject(User user, Project project);

}
