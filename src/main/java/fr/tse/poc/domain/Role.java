package fr.tse.poc.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID of the role", name = "idRole", required = true, value = "123")
    private Long idRole;

    @ApiModelProperty(notes = "Label of the role", name = "labelRole", required = true, value = "User")
    private String labelRole;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @JsonBackReference
    @ApiModelProperty(notes = "List of users with the role", name = "users")
    private Set<User> users;
}
