package com.fan.myadmin.controller;

import com.fan.myadmin.annotation.AnonymousAccess;
import com.fan.myadmin.entity.Role;
import com.fan.myadmin.entity.User;
import com.fan.myadmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author fanweiwei
 * @create 2020-04-02 22:50
 */
@RestController
public class TestController {


    @Autowired
    private UserService userService;

    @GetMapping("/emp/basic/test")
    public String test1(){
        return "员工资料接口";
    }

    @GetMapping("/emp/recruitment/test")
    public String test2(){
        return "员工招聘接口";
    }

    @GetMapping("/salary/change/test")
    public String test3(){
        return "工资调整接口";
    }

    @GetMapping("/salary/statistics/test")
    public String test4(){
        return "工资统计接口";
    }

    @GetMapping("/per/evaluation/test")
    public String test5(){
        return "业绩考核接口";
    }

    @GetMapping("/per/statistics/test")
    public String test6(){
        return "业绩统计接口";
    }

    @AnonymousAccess
    @GetMapping("/test")
    public String test7(HttpServletRequest request){
        //1.从HttpServletRequest中获取SecurityContextImpl对象
        SecurityContext securityContext = (SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        //2.从SecurityContextImpl中获取Authentication对象
        Authentication authentication = securityContext.getAuthentication();
        //3.初始化UsernamePasswordAuthenticationToken实例 ，这里的参数user就是我们要更新的用户信息


        User admin = userService.findByUsername("admin");
        Set<Role> roles = admin.getRoles();
        System.out.println(admin);
        return "登陆就能访问的接口";
    }
}
