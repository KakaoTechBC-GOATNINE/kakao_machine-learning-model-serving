package com.example.kakao_mlms.util;

import com.example.kakao_mlms.constant.Constants;
import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.ERole;
import com.example.kakao_mlms.dto.response.JwtTokenDto;
import com.example.kakao_mlms.exception.CommonException;
import com.example.kakao_mlms.exception.ErrorCode;
import com.example.kakao_mlms.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.Objects;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil implements InitializingBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final UserRepository userRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token-expire-period}")
    private Integer accessTokenExpiredPeriod;
    @Getter
    @Value("${jwt.refresh-token-expire-period}")
    private Integer refreshTokenExpiredPeriod;
    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public int getWebRefreshTokenExpirationSecond() {
        return (int) (refreshTokenExpiredPeriod / 1000);
    }

    public JwtTokenDto generateTokens(final Long id, final ERole role) {
        final String accessToken = generateToken(id, role, accessTokenExpiredPeriod * 1000);
        final String refreshToken = generateToken(id, role, refreshTokenExpiredPeriod * 1000);
        return new JwtTokenDto(accessToken, refreshToken);
    }

    public String generateToken(final Long id, final ERole role, final Integer expirationPeriod) {
        final Claims claims = Jwts.claims();
        claims.put(Constants.USER_ID_CLAIM_NAME, id.toString());
        claims.put(Constants.USER_ROLE_CLAIM_NAME, role.toString());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationPeriod))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateJwtToken(Authentication authentication) {
        final Claims claims = Jwts.claims();
        claims.put(Constants.USER_ID_CLAIM_NAME, String.valueOf(authentication.getPrincipal()));
        claims.put(Constants.USER_ROLE_CLAIM_NAME, authentication.getAuthorities());

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpiredPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims validateToken(final String token) throws ExpiredJwtException, JwtException {
        final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        return jwtParser.parseClaimsJws(token).getBody();
    }

    /**
     * Request Header에서 토큰 추출
     */
    public static String refineToken(HttpServletRequest request) throws JwtException {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        } else {
            throw new IllegalArgumentException("Not Valid Or Not Exist Token");
        }

    }

    @Transactional
    public JwtTokenDto reissue(final String refreshToken) {
        final Claims claims = validateToken(refreshToken);

        final Long id = Long.valueOf(claims.get(Constants.USER_ID_CLAIM_NAME).toString());
        final ERole role = ERole.of(claims.get(Constants.USER_ROLE_CLAIM_NAME).toString());

        final User user = userRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        if (!Objects.equals(user.getId(), id) || user.getRole() != role || !user.getRefreshToken().equals(refreshToken)) {
            log.error("Invalid Token");
            throw new CommonException(ErrorCode.INVALID_TOKEN_ERROR);
        }

        final JwtTokenDto jwtTokenDto = generateTokens(id, role);

        user.updateRefreshToken(jwtTokenDto.getRefreshToken());

        return jwtTokenDto;
    }
}
