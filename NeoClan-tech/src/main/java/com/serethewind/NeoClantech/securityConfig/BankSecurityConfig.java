package com.serethewind.NeoClantech.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class BankSecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /**
         * disable csrf. cross site request forgery allows an hacker take control from you after authenticating yourself.
         */
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) -> {
                    authorizeHttpRequests.requestMatchers(HttpMethod.GET, "/api/v1/users").permitAll();
                    authorizeHttpRequests.anyRequest().authenticated();
                }).httpBasic(Customizer.withDefaults());


//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeRequests()
//                .requestMatchers(HttpMethod.GET, "/api/v1/users").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic(withDefaults());

        return http.build();
    }


    //working with in-memory database
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails firstUser = User.withUsername("noah").password(passwordEncoder().encode("1234")).roles("ADMIN").build();
        UserDetails secondUser = User.withUsername("mimi").password(passwordEncoder().encode("12345")).roles("USER").build();

        return new InMemoryUserDetailsManager(firstUser, secondUser);
    }

    //in-memory requires a password encoder to save the password

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
