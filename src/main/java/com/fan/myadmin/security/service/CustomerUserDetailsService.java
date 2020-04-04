package com.fan.myadmin.security.service;

import com.fan.myadmin.entity.Menu;
import com.fan.myadmin.entity.Role;
import com.fan.myadmin.repository.UserRepository;
import org.springframework.security.core.userdetails.User;

import com.fan.myadmin.security.SecurityUser;
import com.fan.myadmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author fanweiwei
 * @create 2020-04-01 21:59
 */
@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.fan.myadmin.entity.User user = userRepository.findByUsername(username);
        Set<Role> roles = user.getRoles();
       // List<String> permissions = new ArrayList<>();
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if(roles!=null){
            for(Role role:roles){
                String permission = role.getPermission();
                if(permission!=null&&permission.trim()!=""){
                    GrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission);
                    grantedAuthorities.add(simpleGrantedAuthority);
                }
                Set<Menu> menus = role.getMenus();
                if(menus!=null){
                    for(Menu menu:menus){
                        String permission1 = menu.getPermission();
                        if(permission1!=null&&permission1.trim()!=""){
                            GrantedAuthority simpleGrantedAuthority;
                            simpleGrantedAuthority = new SimpleGrantedAuthority(permission1);
                            grantedAuthorities.add(simpleGrantedAuthority);
                        }

                    }

                }

            }
            return new User(username, passwordEncoder.encode(user.getPassword()),grantedAuthorities );

        }else{
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }

    }
}
