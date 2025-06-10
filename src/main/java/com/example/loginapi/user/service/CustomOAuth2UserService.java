package com.example.loginapi.user.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request)
            throws OAuth2AuthenticationException {
        OAuth2User oauthUser = new DefaultOAuth2UserService().loadUser(request);

        Map<String, Object> originalAttributes = oauthUser.getAttributes();
        Map<String, Object> attributes = new HashMap<>(originalAttributes);

        String githubId = attributes.get("id").toString();
        String login    = (String) attributes.get("login");

        String email = fetchEmailIfNeeded(attributes, login);
        attributes.put("email", email);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                attributes,
                "login"
        );
    }

    private String fetchEmailIfNeeded(Map<String, Object> attributes, String login) {
        Object emailObj = attributes.get("email");
        if (emailObj instanceof String && !((String) emailObj).isBlank()) {
            return (String) emailObj;
        }
        String fakeEmail = login + "@github.com";
        attributes.put("email", fakeEmail);
        return fakeEmail;
    }


}
