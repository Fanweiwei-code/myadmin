package com.fan.myadmin.security.config;

import com.fan.myadmin.annotation.AnonymousAccess;
import com.fan.myadmin.security.service.CustomerUserDetailsService;
import com.fan.myadmin.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
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

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

//    @Bean//PersistentTokenRepository记住我功能的工具类
//    public PersistentTokenRepository persistentTokenRepository(){
//        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
//        tokenRepository.setDataSource(dataSource);
//        return tokenRepository;
//    }


    @Bean
    public PasswordEncoder securityPasswordEncoding(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        Set<String> anonymousUrls = new HashSet<>();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Set<Map.Entry<RequestMappingInfo, HandlerMethod>> entries = handlerMethods.entrySet();
        for(Map.Entry<RequestMappingInfo, HandlerMethod> entryMap:entries){
            AnonymousAccess methodAnnotation = entryMap.getValue().getMethodAnnotation(AnonymousAccess.class);
            if(methodAnnotation!=null){
                Set<String> patterns = entryMap.getKey().getPatternsCondition().getPatterns();
                Iterator<String> iterator = patterns.iterator();
                while (iterator.hasNext()){
                    web.ignoring().antMatchers(iterator.next());
                }
            }

        }
        web.ignoring().antMatchers( "/static/**","/resources/**",
                "/**/*.png ", "/**/*.jpg", "/**/*.gif ", "/**/*.svg", "/**/*.ico", "/**/*.ttf", "/**/*.woff",
                "/api-docs/**", "/v2/api-docs/**",
                "/webjars/**","/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       // super.configure(http);
        http
                .formLogin()
                //指定登录页的路径
                .loginPage("/login")
                //指定自定义form表单请求的路径
                .loginProcessingUrl("/user/login")
                .successForwardUrl("/hello")
                .permitAll()
//              .and()
//              .logout().deleteCookies("SESSION", "remember-me")
//              .and().rememberMe().alwaysRemember(true).tokenRepository(persistentTokenRepository())
//              .tokenValiditySeconds(60 * 60 * 24 * 7)  //设置记住我的时间为7天
                .and()//异常处理
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler)
                .and()
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
                .sessionManagement()
                .invalidSessionUrl("/logout")
                //设置单点登录
                .maximumSessions(1)
                .expiredSessionStrategy(new SessionInformationExpiredStrategy() {
                    @Override
                    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
                        ResponseUtils.write(sessionInformationExpiredEvent.getResponse(), "您的账号在另一地点登录！");
                    }
                })
                .and()
                .and()
                .csrf().disable();

       ;

    }

}
