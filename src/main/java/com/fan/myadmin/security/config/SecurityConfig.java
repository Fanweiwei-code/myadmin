package com.fan.myadmin.security.config;

import com.fan.myadmin.annotation.AnonymousAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author fanweiwei
 * @create 2020-04-01 20:42
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public PasswordEncoder securityPasswordEncoding(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //获取每一个匿名访问注解方法上的url
        Set<String> anonymousUrls = new HashSet<>();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Set<Map.Entry<RequestMappingInfo, HandlerMethod>> entries = handlerMethods.entrySet();
        for(Map.Entry<RequestMappingInfo, HandlerMethod> entryMap:entries){
            AnonymousAccess methodAnnotation = entryMap.getValue().getMethodAnnotation(AnonymousAccess.class);
            if(methodAnnotation!=null){
                anonymousUrls.addAll(entryMap.getKey().getPatternsCondition().getPatterns());
            }

        }

        //super.configure(http);
       http
               .authorizeRequests()
               .antMatchers("/login","/login.html").permitAll()
               .and()
               .formLogin().loginPage("/login")
               .loginProcessingUrl("/index")
               .defaultSuccessUrl("/index")
               .and()
               //静态资源
               .authorizeRequests().antMatchers(
                   HttpMethod.GET,
                   "/**/*.css",
                   "/**/*.js"
                ).permitAll()
               //匿名访问
               .antMatchers(anonymousUrls.toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
       ;

    }
}
