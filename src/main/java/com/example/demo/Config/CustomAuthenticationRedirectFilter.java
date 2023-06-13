package com.example.demo.Config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationRedirectFilter extends OncePerRequestFilter {
    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/login");
    private final String redirectUrl;
    public CustomAuthenticationRedirectFilter(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(requestMatcher.matches(request) && isAuthenticated()) {
            response.sendRedirect(redirectUrl);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
