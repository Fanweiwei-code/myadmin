package com.fan.myadmin.security.config;

import com.fan.myadmin.entity.Menu;
import com.fan.myadmin.entity.Role;
import com.fan.myadmin.service.MenuService;
import javafx.scene.effect.SepiaTone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author fanweiwei
 * @create 2020-04-02 21:42
 */
@Component
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuService menuService;

    private AntPathMatcher antPathMatcher =new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取请求的url
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        //获取当前请求需要的角色信息，拿url去menu表中匹配，查看是访问的那个menu下的资源，根据menuId 获取到role信息
        List<Menu> menus = menuService.queryAll();
        for(Menu menu:menus){
            //如果请求的路径包含在某个menu的url中，且能访问该资源的角色信息存在
            if(antPathMatcher.match(menu.getPath(),requestUrl) && menu.getRoles().size() > 0){
                List<Role> roles = new ArrayList<>( menu.getRoles());
                int size = roles.size();
                //定义一个数组，来接收能访问该资源的角色
                String[] roleNameArray = new String[size];
                for (int i = 0; i < size; i++) {
                    roleNameArray[i] = roles.get(i).getPermission();
                }
                SecurityConfig.createList(roleNameArray);
                return SecurityConfig.createList(roleNameArray);

            }

        }
        //如果遍历完menu之后没有匹配上，说名访问该资源不需要权限信息，设置一个登陆就能访问的角色
        return SecurityConfig.createList("ROLE_login");
   //     throw new AccessDeniedException("权限不足");
      //  return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
