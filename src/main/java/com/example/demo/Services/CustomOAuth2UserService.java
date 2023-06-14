package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class
CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultUserService = new DefaultOAuth2UserService();
        OAuth2User oauth2User = defaultUserService.loadUser(userRequest);
        String emailAttributeName = "email"; // Assuming email is one of the attributes returned by the OAuth2 provider
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get(emailAttributeName);

        // Check if the user exists in the database based on the email
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails != null) {
            Set<OAuth2UserAuthority> authorities = new HashSet<>();
            authorities.add(new OAuth2UserAuthority(attributes));
            return new DefaultOAuth2User(authorities, attributes, emailAttributeName);
        } else {
            throw new OAuth2AuthenticationException("User does not exist");
        }
    }
}
