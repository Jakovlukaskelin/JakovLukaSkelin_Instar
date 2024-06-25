package hr.instar.instar.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] resources = new String[]{
                "/",
                "/store/**",
                "/auth/login",
                "/auth/register",    "/static/image/**",
                "/api/isLoggedIn"
        };

        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/store/checkout/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/store/purchases/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(resources).permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .failureUrl("/auth/login?error=true")
                        .loginProcessingUrl("/auth/login/process")
                        .defaultSuccessUrl("/store", true)
                        .permitAll())
                .logout((logout) -> logout
                        .permitAll()
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/store")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        final String sqlUserName = "select u.username, u.password, u.enabled from [users] u where u.username = ?";
        final String sqlAuthorities = "select a.username, a.authority from [authorities] a where a.username = ?";

        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(sqlUserName)
                .authoritiesByUsernameQuery(sqlAuthorities)
                .passwordEncoder(passwordEncoder);
    }
}
