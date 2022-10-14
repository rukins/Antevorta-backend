package com.inventorymanagementsystem.security.config;

import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.repository.UserRepository;
import com.inventorymanagementsystem.security.provider.CustomAuthenticationProvider;
import com.inventorymanagementsystem.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService userDetailsService;

     @Autowired
     private CustomAuthenticationProvider authenticationProvider;

     @Autowired
     private AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf().disable()

                    .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)

                    .and()

                    .authorizeRequests()
                    .antMatchers(
                            HttpMethod.POST,
                            "/auth/login", "auth/logout", "/signup"
                    )
                    .permitAll()

                    .anyRequest()
                    .authenticated()

                    .and()

                    .logout()
                    .deleteCookies("JSESSIONID")

                .and()

                    .userDetailsService(userDetailsService)
                    .authenticationProvider(authenticationProvider);

            return http.build();
    }

    @Bean
    public ApplicationRunner dataLoader(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            userRepository.save(new User("first@gmail.com", encoder.encode("password"), "firstname", "lastname"));
            userRepository.save(new User("second@icloud.com", encoder.encode("password"), "firstname", "lastname"));
        };
    }
}
