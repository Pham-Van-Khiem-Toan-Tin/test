package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConf {
    @Autowired
    private CustomAuthenticationSuccessHandler customSuccessHandler;
    @Autowired
    CustomAuthenticationFailureHandler customFailureHandler;
    @Autowired
    CustomOauth2LoginSuccessHandler customOauth2LoginSuccessHandler;
    @Autowired
    ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    CustomLogoutSuccessHandler customLogoutSuccessHandler;
    @Autowired
    JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/", "/oauth2/authorization/google/**", "/register").permitAll()
                .antMatchers("/admin").hasAnyAuthority("ADMIN")
                .antMatchers("/user").hasAnyAuthority( "USER", "ROLE_USER")
                .antMatchers("/test/").permitAll()
                .anyRequest().authenticated()
            .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new CustomAuthenticationRedirectFilter("/user"), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .successHandler(customSuccessHandler)
            .and()
                .oauth2Login()
                    .authorizationEndpoint()
                    .authorizationRequestResolver(new CustomAuthorizationRequestResolver(this.clientRegistrationRepository))
                    .and()
                .loginPage("/login")
                .permitAll()
                .successHandler(customOauth2LoginSuccessHandler)
                .and()
                .rememberMe()
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .permitAll()
            .and()
                .rememberMe();

        return http.build();
    }

}
