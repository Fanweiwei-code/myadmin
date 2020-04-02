package com.fan.myadmin.security.config;

import com.fan.myadmin.annotation.AnonymousAccess;
import com.fan.myadmin.security.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.sql.DataSource;
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
    private DataSource dataSource;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private MySecurityMetadataSource mySecurityMetadataSource;

    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;

    @Bean//PersistentTokenRepository记住我功能的工具类
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    //注册UserDetailsService 的bean
    @Bean
    UserDetailsService customUserService(){
        return new CustomerUserDetailsService();
    }

    @Bean
    public PasswordEncoder securityPasswordEncoding(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers( "/login_p.html", "/authentication/request","/favicon.ico","/code/image");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService());
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
//        http
//                //表单登陆配置
//                .formLogin()
//                //.loginPage("/authentication/request").loginProcessingUrl("/user/login")
//                //.usernameParameter("username").passwordParameter("password")
//
//                .and()
//                //记住我配置
//                .rememberMe()
//                .tokenRepository(persistentTokenRepository())
//                .tokenValiditySeconds(20)
//                .userDetailsService(userDetailsService())
//                .and()// 关闭跨站请求伪造防护
//                .csrf().disable();

        super.configure(http);
       http
               .authorizeRequests()
               .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                   @Override
                   public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                       o.setSecurityMetadataSource(mySecurityMetadataSource);
                       o.setAccessDecisionManager(myAccessDecisionManager);
                       return o;
                   }
               })
               .and()
               .authorizeRequests()
               .antMatchers("/login","/login.html").permitAll()
               .and()
               .formLogin()/*.loginPage("/login")
               .loginProcessingUrl("/index")
               .defaultSuccessUrl("/index")*/
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
