package com.fan.myadmin.security.service;

import com.fan.myadmin.entity.Role;
import com.fan.myadmin.entity.User;
import com.fan.myadmin.security.SecurityUser;
import com.fan.myadmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * @author fanweiwei
 * @create 2020-04-01 21:59
 */
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        Set<Role> roles = user.getRoles();
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();



        //SecurityUser securityUser = new SecurityUser(username,passwordEncoder.encode(user.getPassword()),);


        return null;
    }
}
