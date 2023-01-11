package fr.tse.poc.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {

    private Long idUser;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

}
