package com.fan.myadmin.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author fanweiwei
 * @create 2020-04-01 22:06
 */
@Getter
@AllArgsConstructor
public class SecurityUser implements UserDetails {

    private final String username;
    @JsonIgnore
    private final String password;

    private final Collection<GrantedAuthority> authorities;

    private final boolean isEnabled;

    private final boolean isCredentialsNonExpired;

    private final boolean isAccountNonLocked;

    private final boolean isAccountNonExpired;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
