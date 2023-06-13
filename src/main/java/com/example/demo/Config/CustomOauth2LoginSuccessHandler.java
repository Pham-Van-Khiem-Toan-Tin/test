package com.example.demo.Config;

import com.example.demo.Services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomOauth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    JWTuntil jwTuntil;
    private OAuth2AuthorizedClientService authorizedClientService;

    public CustomOauth2LoginSuccessHandler(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oauth2User = oauthToken.getPrincipal();
            String username = oauth2User.getAttribute("name");
            String email = oauth2User.getAttribute("email");
            Collection<?> autho = oauth2User.getAuthorities();
            // Sử dụng AccessToken tùy ý trong ứng dụng của bạn
            // Ví dụ: Lưu trữ AccessToken vào cơ sở dữ liệu hoặc sử dụng cho các yêu cầu API bổ sung
            String[] parts = email.split("@");
            String domain = parts[1];
            domain = "vnpay.vn";
            System.out.println("Domain: " + domain);
            if (!domain.equals("vnpay.vn")) {
                String logoutUrl = "https://accounts.google.com/o/oauth2/revoke?token=";
                OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
                String accessToken = client.getAccessToken().getTokenValue();
                // Send a GET request to the Google OAuth2 logout URL with the access token as a parameter
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getForObject(logoutUrl + accessToken, String.class);
                throw new OAuth2AuthenticationException("Gmail is not in company");
            } else if (employeeService.findByEmail(parts[0] + "@vnpay.vn") != null) {
                throw new OAuth2AuthenticationException("Email is not in department");
            }
            String accessToken = jwTuntil.generateAccessToken(email);
            String refreshToken = jwTuntil.generateRefeshToken(email);
            Cookie refreshTokenCookie = new Cookie("rfToken", refreshToken);
            refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60); // Thời gian sống của cookie (30 ngày)
            refreshTokenCookie.setPath("/"); // Đường dẫn áp dụng cookie trên toàn bộ ứng dụng
            refreshTokenCookie.setHttpOnly(true);
            Cookie accessTokenCookie = new Cookie("acToken","Bearer" + accessToken);
            accessTokenCookie.setMaxAge(30 * 24 * 60 * 60); // Thời gian sống của cookie (30 ngày)
            accessTokenCookie.setPath("/"); // Đường dẫn áp dụng cookie trên toàn bộ ứng dụng
            accessTokenCookie.setHttpOnly(true);
            String message = "Welcome " + username + " logined";
            request.getSession().setAttribute("message", message );
            response.addCookie(refreshTokenCookie);
            response.addCookie(accessTokenCookie);
            //            response.setHeader("Authorization" , "Bear " + accessToken);
        } else {
            throw new OAuth2AuthenticationException("Invalid authentication type");
        }
        response.sendRedirect("/user");
    }
}


