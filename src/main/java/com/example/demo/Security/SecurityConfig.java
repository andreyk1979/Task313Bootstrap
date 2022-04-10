package com.example.demo.Security;

import com.example.demo.Service.RoleService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    private SuccessUserHandler successUserHandler; // класс, в котором описана логика перенаправления пользователей по ролям
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean //https://www.youtube.com/watch?v=HvovW6Uh1yU "Как устроен Spring Security" - 31.05.20 1:08:00
    public static BCryptPasswordEncoder passwordEncoder() { //преобразователь паролей вбиваемых в форму, для дальнейшего сравнения пароля, хранеящегося в БД
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() { // при получении логинаи пароля с формы - задача этого
        //этого провайдера сказать, существует ли такой пользователь или не существует.
        //и если существет, то его надо положить в SpringSecurity COntext
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder()); //использует преобразователь паролей (см выше) для дальнейшего сравнения пароля, хранеящегося в БД
        authProvider.setUserDetailsService(userDetailsService); //предоставляет User_ов из БД для сравнения с вбитыми из вэб формы
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//
                .antMatchers("/user/**", "/webjars/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/**").hasRole("ADMIN")
                .and()
                .formLogin()
                .usernameParameter("email")
                .successHandler(successUserHandler)
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .csrf()
                .disable();

/*    @PostConstruct
    private void postConstruct() {
        User admin = new User("andr", "andr1000", "andrey@mail.ru", "100",
                roleService.getAllRoles());
        userService.save(admin);
    }*/
    }

}