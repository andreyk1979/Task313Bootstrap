package com.kuimov.pp.task313.Security;


import com.kuimov.pp.task313.Service.RoleService;
import com.kuimov.pp.task313.Service.UserDetailsServiceImpl;
import com.kuimov.pp.task313.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService; // используется только для Создание админа в БД
    private final RoleService roleService; // используется только для Создание админа в БД

    private final SuccessUserHandler successUserHandler; // класс, в котором описана логика перенаправления пользователей по ролям

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public SecurityConfig(UserService userService, RoleService roleService, SuccessUserHandler successUserHandler, UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.roleService = roleService;
        this.successUserHandler = successUserHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//
                .antMatchers("/user/**", "/login", "/webjars/**").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/**").hasAuthority("ADMIN")
                .and()
                .formLogin().loginPage("/login").permitAll()
                .successHandler(successUserHandler)
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .csrf()
                .disable();
    }
/* для автоматического создания админа (включать только после создания таблицы через create)
    @PostConstruct
    private void postConstruct() {
        User admin = new User("Andrey", "Kuimov", 43, "kuimow@mail.ru", "123456", roleService.getAllRoles());
        userService.save(admin);
    }*/
}