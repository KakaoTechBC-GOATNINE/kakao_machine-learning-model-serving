package com.example.kakao_mlms.security.service;
import com.nimbusds.jose.shaded.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Service
public class KakaoLogoutService {

    private static final String KAKAO_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";
    private static final Logger log = LoggerFactory.getLogger(KakaoLogoutService.class);

    public void logoutFromKakao(String accessToken) {

        final RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = new HttpEntity<>(parameters, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_LOGOUT_URL,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
    }
}


