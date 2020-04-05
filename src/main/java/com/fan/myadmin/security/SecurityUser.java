package com.fan.myadmin.security;

import com.fan.myadmin.entity.Menu;
import com.fan.myadmin.entity.Role;
import com.fan.myadmin.service.MenuService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author fanweiwei
 * @create 2020-04-01 22:06
 */
@Getter
public class SecurityUser implements UserDetails {

    private final String username;
    @JsonIgnore
    private final String password;

    private final Collection<GrantedAuthority> authorities;

    private final Menu menus;

    private Set<Role> roles;

    public SecurityUser(String username, String password, Collection<GrantedAuthority> authorities, Menu menus) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.menus = menus;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
