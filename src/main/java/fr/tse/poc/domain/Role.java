package fr.tse.poc.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue
    private int idRole;

    private String labelRole;

    @OneToMany(mappedBy = "role")
    private Set<User> users;
}
