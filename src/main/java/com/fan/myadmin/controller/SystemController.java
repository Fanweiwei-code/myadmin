package com.fan.myadmin.controller;

import com.fan.myadmin.annotation.AnonymousAccess;
import com.fan.myadmin.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fanweiwei
 * @create 2020-04-01 20:34
 */
@Controller
//@RequestMapping("/system")
public class SystemController {

    @RequestMapping("/login")
    public String toLogin(){
        return "login";
    }

    @AnonymousAccess
    @RequestMapping("/index")
    public String toIndex(){
        return "index";
    }
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "你有权限-----哈哈哈！";
    }

    @RequestMapping("/xiaoming")
    @ResponseBody
    public String xiaoming(){
        return "你有权限-----我是小明！";
    }

}
