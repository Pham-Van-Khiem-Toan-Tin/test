package com.example.demo.Config;

import com.example.demo.Services.EmployeeService;
import com.nimbusds.jose.Algorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

@Component
public class JWTuntil {
    @Value("${jwt.accessTokenSecret}")
    private String accessSecretKey;
    @Value("${jwt.accessTokenExpiration}")
    private int accessExpries;
    @Value("${jwt.refeshTokenSecret}")
    private String refeshSecretKey;
    @Value("${jwt.refreshTokenExpiration}")
    private int refeshExpires;
    @Autowired
    EmployeeService employeeService;

    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessExpries);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, accessSecretKey)
                .compact();
    }
    public String generateRefeshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refeshExpires);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, refeshSecretKey)
                .compact();
    }
    public String getUserFromToken(String token, String secret) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public void removeToken(HttpServletResponse response) {
        Cookie rfToken = new Cookie("rfToken", null);
        rfToken.setMaxAge(0);
        rfToken.setPath("/");
        response.addCookie(rfToken);
        Cookie acToken = new Cookie("acToken", null);
        acToken.setMaxAge(0);
        acToken.setPath("/");
        response.addCookie(acToken);
    }
    public UserDetails getUserDetailToken(String email) {
         return employeeService.loadUserByUsername(email);
    }
    public Boolean validateToken(String token, String secret) {
        try {
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
