package com.example.kakao_mlms.util;

import com.example.kakao_mlms.exception.CommonException;
import com.example.kakao_mlms.exception.ErrorCode;
import com.nimbusds.jose.shaded.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OAuth2Util {
    @Value("${client.provider.kakao.authorization-uri: aaa.bbb.ccc}")
    private String KAKAO_AUTHORIZATION_URL;
    @Value("${client.provider.kakao.token-uri: aaa.bbb.ccc}")
    private String KAKAO_TOKEN_URL;
    @Value("${client.provider.kakao.user-info-uri: aaa.bbb.ccc}")
    private String KAKAO_USERINFO_URL;
    @Value("${client.registration.kakao.client-id: aaa.bbb.ccc}")
    private String KAKAO_CLIENT_ID;
    @Value("${client.registration.kakao.redirect-uri: aaa.bbb.ccc}")
    private String KAKAO_REDIRECT_URL;

    public String getKakaoRedirectUrl() {
        return KAKAO_AUTHORIZATION_URL
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    public String getKakaoUserInformation(final String accessToken) throws CommonException {
        final WebClient webClient = WebClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.setBearerAuth(accessToken);
                    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .build();

        final String responseJsonBody = webClient.post()
                .uri(KAKAO_USERINFO_URL)
                .retrieve()

//                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
//                        clientResponse -> Mono.just(new CommonException()))
                .bodyToMono(String.class)
                .flux()
                .toStream()
                .findFirst()
                .orElseThrow(() -> new CommonException(ErrorCode.AUTH_SERVER_USER_INFO_ERROR));

        return JsonParser.parseString(responseJsonBody)
                .getAsJsonObject()
                .get("id")
                .getAsString();
    }

    public String getKakaoAccessToken(final String authorizationCode) {
        final RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", KAKAO_CLIENT_ID);
        parameters.add("redirect_uri", KAKAO_REDIRECT_URL);
        parameters.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = new HttpEntity<>(parameters, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_TOKEN_URL,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        return JsonParser.parseString(response.getBody()).getAsJsonObject().get("access_token").getAsString();
    }
}
