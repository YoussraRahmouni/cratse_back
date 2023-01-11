package fr.tse.poc.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String job;

    @NotBlank
    @Email
    private String email;

    private Long role;

    @NotBlank
    private String password;
}
