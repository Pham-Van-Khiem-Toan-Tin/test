package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JWTuntil jwTuntil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String username = authentication.getName();
        String message = "Welcome " + username + " logined";
        String accessToken = jwTuntil.generateAccessToken(username);
        String refreshToken = jwTuntil.generateRefeshToken(username);

        Cookie refreshTokenCookie = new Cookie("rfToken", refreshToken);
        refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60); // Thời gian sống của cookie (30 ngày)
        refreshTokenCookie.setPath("/"); // Đường dẫn áp dụng cookie trên toàn bộ ứng dụng
        refreshTokenCookie.setHttpOnly(true); // Cookie chỉ có thể được truy cập thông qua HTTP, không thể truy cập bằng JavaScript
        // Trả về Access Token và Refresh Token trong response
        Cookie accessTokenCookie = new Cookie("acToken","Bearer" + accessToken);
        accessTokenCookie.setMaxAge(30 * 24 * 60 * 60); // Thời gian sống của cookie (30 ngày)
        accessTokenCookie.setPath("/"); // Đường dẫn áp dụng cookie trên toàn bộ ứng dụng
        accessTokenCookie.setHttpOnly(true);
        response.addCookie(refreshTokenCookie);
        response.addCookie(accessTokenCookie);
//        response.setHeader("Authorization" , "Bear " + accessToken);
        request.getSession().setAttribute("message", message);
        // Redirect to the desired page
        response.sendRedirect("/user");

    }
}
