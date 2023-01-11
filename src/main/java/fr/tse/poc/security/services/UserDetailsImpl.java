package fr.tse.poc.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.tse.poc.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long idUser;

    private String firstName;

    private String lastName;

    private String email;

    @JsonIgnore
    private String password;

    private String job;

    private Collection<? extends GrantedAuthority> role;


    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority(user.getRole().getLabelRole()));
        return new UserDetailsImpl(
                user.getIdUser(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getJob(),
                role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
