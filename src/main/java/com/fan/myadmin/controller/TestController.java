package com.fan.myadmin.controller;

import com.fan.myadmin.annotation.AnonymousAccess;
import com.fan.myadmin.entity.Role;
import com.fan.myadmin.entity.User;
import com.fan.myadmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String test7(){

        User admin = userService.findByUsername("admin");
        Set<Role> roles = admin.getRoles();
        System.out.println(admin);
        return "登陆就能访问的接口";
    }
}
