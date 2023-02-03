package fr.tse.poc.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(unique = true)
    private String email;

    // To hide password from GET response
    @JsonIgnore
    private String password;

    private String job;

    @ManyToOne
    @JsonManagedReference
    private Role role;

    @ManyToOne
    @JsonManagedReference
    private User manager;

    @OneToMany(mappedBy = "manager")
    @JsonBackReference
    private Set<User> managedUsers;

}
