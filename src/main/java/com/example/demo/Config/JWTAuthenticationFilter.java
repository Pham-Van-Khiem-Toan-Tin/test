package com.example.demo.Config;

import com.example.demo.Config.JWTuntil;
import com.example.demo.Model.Employee;
import com.example.demo.Services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTuntil jwTuntil;

    @Value("${jwt.accessTokenSecret}")
    private String accessSecretKey;
    @Value("${jwt.refeshTokenSecret}")
    private String refeshSecretKey;
    @Autowired
    EmployeeService employeeService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String accessToken = request.getHeader("Authorization");
        String refreshToken = getRefreshTokenFromCookie(request, "rfToken");
        String accessToken = getRefreshTokenFromCookie(request, "acToken");

        if(StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer")) {
            accessToken = accessToken.substring(7);
            if(jwTuntil.validateToken(accessToken,accessSecretKey)) {
                String username = jwTuntil.getUserFromToken(accessToken, accessSecretKey);
                UserDetails userDetails = jwTuntil.getUserDetailToken(username);
                Authentication authenticated = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticated);
            } else if(StringUtils.hasText(refreshToken)){
                if(jwTuntil.validateToken(refreshToken, refeshSecretKey)) {
                    String username = jwTuntil.getUserFromToken(refreshToken, refeshSecretKey);
                    String newAccessToken = jwTuntil.generateAccessToken(username);
                    UserDetails userDetails = jwTuntil.getUserDetailToken(username);
                    Authentication authenticated = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authenticated);
                }   else  {
                    jwTuntil.removeToken(response);
                    response.sendRedirect("/login");
                    return;
                }
            } else {
                response.sendRedirect("/login");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    private String getRefreshTokenFromCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
