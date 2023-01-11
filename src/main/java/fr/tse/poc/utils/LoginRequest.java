package fr.tse.poc.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
