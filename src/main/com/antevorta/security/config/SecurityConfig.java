package com.antevorta.security.config;

import com.antevorta.model.Authority;
import com.antevorta.model.User;
import com.antevorta.repository.AuthorityRepository;
import com.antevorta.repository.UserRepository;
import com.antevorta.security.encryptor.Encryptor;
import com.antevorta.security.provider.CustomAuthenticationProvider;
import com.antevorta.security.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JpaUserDetailsService userDetailsService;
    private final CustomAuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public SecurityConfig(JpaUserDetailsService userDetailsService, CustomAuthenticationProvider authenticationProvider,
                          AuthenticationEntryPoint authenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authenticationProvider = authenticationProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

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
    public ApplicationRunner dataLoader(UserRepository userRepository, AuthorityRepository authorityRepository, Encryptor encryptor) {
        return args -> {
            Authority hasMultipleStoresByTypeAuthority = new Authority("hasMultipleStoresByType");

            authorityRepository.save(hasMultipleStoresByTypeAuthority);

            userRepository.save(new User("first@gmail.com", encryptor.encrypt("password"), "firstname", "lastname"));
            userRepository.save(new User("second@icloud.com", encryptor.encrypt("password"), "firstname", "lastname", Set.of(hasMultipleStoresByTypeAuthority)));
        };
    }
}
