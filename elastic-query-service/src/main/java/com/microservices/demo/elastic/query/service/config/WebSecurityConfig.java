package com.microservices.demo.elastic.query.service.config;

import com.microservices.demo.config.UserConfigData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // Vem do módulo app-config-data
    private final UserConfigData userConfigData;

    public WebSecurityConfig(UserConfigData userData) {
        this.userConfigData = userData;
    }

    // Propriedade do arquivo config-client-elastic_query.yml no módulo (Github): config-server-repository
    @Value("${security.paths-to-ignore}")
    private String[] pathsToIgnore;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/**").hasRole("USER")
                .and()
                .csrf().disable();  // Desabilitar Csrf indica a falsificação de solicitação entre sites
        // CSRF -> An attack that uses already authenticated user's session to do unwanted actions,
        // triggering it from browser.
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity
                .ignoring()
                .antMatchers(pathsToIgnore);  // Ignorar os endpoints do Swagger
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // password: "{noop}test1234" -> {noop} para usar a senha como texto sem formatação
        auth.inMemoryAuthentication()
                .withUser(userConfigData.getUsername())
                .password(passwordEncoder().encode(userConfigData.getPassword()))
                .roles(userConfigData.getRoles());
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        // Esta é uma função adaptativa e usa contagem de iteração para aumentar a dificuldade
        // contra ataques de força bruta além de usar um salt(sal) para proteger contra ataques
        //  de mesa arco-íris (rainbow table)
        return new BCryptPasswordEncoder();
    }


}
