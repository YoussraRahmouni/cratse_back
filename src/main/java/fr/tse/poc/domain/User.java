package fr.tse.poc.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(notes = "ID of the user", name = "id", required = true, value="12345")
    private Long idUser;

    @ApiModelProperty(notes = "First name of the user", name = "firstName", required = true, value="John")
    private String firstName;

    @ApiModelProperty(notes = "Last name of the user", name = "lastName", required = true, value="Doe")
    private String lastName;

    @Column(unique = true)
    @ApiModelProperty(notes = "email of the user", name = "email", required = true, value="john.doe@gmail.com")
    private String email;

    // To hide password from GET response
    @JsonIgnore
    @ApiModelProperty(notes = "Password of the user", name = "password", required = true, value="secret")
    private String password;

    @ApiModelProperty(notes = "Job of the user", name = "job", required = true, value="Developer")
    private String job;

    @ManyToOne
    @JsonManagedReference
    @ApiModelProperty(notes = "Role of the user", name = "role", required = true)
    private Role role;

    @ManyToOne
    @JsonManagedReference
    @ApiModelProperty(notes = "Manager of the user", name = "manager", required = true)
    private User manager;

    @OneToMany(mappedBy = "manager")
    @JsonBackReference
    @ApiModelProperty(notes = "List of users managed by the user", name = "manageUsers")
    private Set<User> managedUsers;

}
