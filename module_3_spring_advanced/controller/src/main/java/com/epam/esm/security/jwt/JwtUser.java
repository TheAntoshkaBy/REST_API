package com.epam.esm.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
public class JwtUser implements UserDetails {

    private final Long id;
    private final String login;
    private final String name;
    private final String surname;
    private final String password;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(
        Long id,
        String login,
        String name,
        String surname,
        String email,
        String password,
        Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }
}
