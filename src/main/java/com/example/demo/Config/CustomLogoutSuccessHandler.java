package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Here for testing purposes
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    JWTuntil jwTuntil;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
        // Construct the Google logout URL
        if(authentication instanceof OAuth2AuthenticationToken) {
            String logoutUrl = "https://accounts.google.com/o/oauth2/revoke?token=";
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
            String accessToken = client.getAccessToken().getTokenValue();
            // Send a GET request to the Google OAuth2 logout URL with the access token as a parameter
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(logoutUrl + accessToken, String.class);
        }
        jwTuntil.removeToken(response);
        SecurityContextHolder.clearContext();
        // Redirect the user to the Google logout URL
        response.sendRedirect("/login");
    }
}
