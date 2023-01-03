package fr.tse.poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    private int idUser;

    private String firstName;

    private String lastName;

    private String email;

    // To hide password from GET response
    @JsonIgnore
    private String password;

    private String job;

    @ManyToOne
    private Role role;

    @ManyToOne
    private User manager;

    @OneToMany(mappedBy = "manager")
    private Set<User> managedUsers;
}
