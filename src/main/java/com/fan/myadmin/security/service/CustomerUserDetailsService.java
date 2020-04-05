package com.fan.myadmin.security.service;

import com.fan.myadmin.entity.Menu;
import com.fan.myadmin.entity.Role;
import com.fan.myadmin.repository.MenuRepository;
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

import java.util.*;

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

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.fan.myadmin.entity.User user = userRepository.findByUsername(username);
        Set<Role> roles = user.getRoles();
       // List<String> permissions = new ArrayList<>();
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Set<Menu> menus = new HashSet<>();
        if(roles!=null){
            for(Role role:roles){
                String permission = role.getPermission();
                if(permission!=null&&permission.trim()!=""){
                    GrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission);
                    grantedAuthorities.add(simpleGrantedAuthority);
                }
                menus.addAll(role.getMenus());
//                if(menus!=null){
//                    for(Menu menu:menus){
//                        String permission1 = menu.getPermission();
//                        if(permission1!=null&&permission1.trim()!=""){
//                            GrantedAuthority simpleGrantedAuthority;
//                            simpleGrantedAuthority = new SimpleGrantedAuthority(permission1);
//                            grantedAuthorities.add(simpleGrantedAuthority);
//                        }
//
//                    }
//
//                }

            }
            Menu m = menuRepository.findById(0l);
            SecurityUser securityUser = new SecurityUser(username,passwordEncoder.encode(user.getPassword()),grantedAuthorities,getMenus(m,menus));
            return securityUser;
            //return new User(username, passwordEncoder.encode(user.getPassword()),grantedAuthorities );

        }else{
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }

    }

    private Menu getMenus(Menu menu ,Set<Menu> ms){
        if(menu!=null){
            List<Menu> menus = menuRepository.findByPid(menu.getId());
            if(menus!=null && menus.size()>0){
                Set<Menu> nms = new HashSet<>();
                for(Menu m : menus){
                    if(!ms.contains(m)){
                        continue;
                    }
                    nms.add(m);
                    getMenus(m,ms);

                }
                menu.setMenus(nms);
            }
        }
        return menu;
    }
}
