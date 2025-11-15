package app.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static app.util.UrlPaths.*;

@Configuration
@EnableMethodSecurity
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(matchers -> matchers
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(URL_ROOT, URL_REGISTER, URL_HOME, URL_CATALOG).permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage(URL_LOGIN)
                        .defaultSuccessUrl(URL_USERS + URL_PROFILE, true)
                        .failureUrl(URL_LOGIN + URL_ERROR)
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher(URL_LOGOUT, "GET"))
                        .logoutSuccessUrl(URL_ROOT)
                );

        return http.build();
    }
}
