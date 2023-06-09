package fr.tse.poc.dao;

import fr.tse.poc.domain.Role;
import fr.tse.poc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<User> findByManager(User user);

    List<User> findByRole(Role role);

}
