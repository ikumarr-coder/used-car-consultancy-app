package com.kik.usedcarconsultancy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${usedcar.admin1.username}")
    private String admin1User;
    @Value("${usedcar.admin1.password}")
    private String admin1Pass;
    @Value("${usedcar.admin2.username")
    private String admin2User;
    @Value("${usedcar.admin2.password}")
    private String admin2Pass;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests( a -> a
                        .requestMatchers("/", "/cars", "/cars/**", "/images/**", "/login", "/css/**", "/js/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
                )
                .formLogin(f -> f.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error")
                        .defaultSuccessUrl("/admin", true)
                        .permitAll()
                )
                .logout(l -> l
                        // .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutRequestMatcher(request -> "GET".equalsIgnoreCase(request.getMethod()) && "/logout".equals(request.getRequestURI()))
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .csrf(c -> c.ignoringRequestMatchers("/h2-console/**"));
        http.headers(h -> h.frameOptions(f -> f.sameOrigin()));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin1 = User.builder()
                .username(admin1User)
                .password(admin1Pass)
                .roles("ADMIN")
                .build();
        UserDetails admin2 = User.builder()
                .username(admin2User)
                .password(admin2Pass)
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin1, admin2);
    }

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
