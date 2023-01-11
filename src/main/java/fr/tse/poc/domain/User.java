package fr.tse.poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

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
