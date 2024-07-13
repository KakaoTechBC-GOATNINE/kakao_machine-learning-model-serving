package com.example.kakao_mlms.dto.response;

import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.EProvider;
import com.example.kakao_mlms.domain.type.ERole;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.example.kakao_mlms.domain.User}
 */
public record UserDto(
        Long id,
        String serialId,
        String password,
        EProvider provider,
        ERole role,
        LocalDate createdDate,
        String nickname,
        Boolean isLogin,
        String refreshToken
) implements Serializable {
    public UserDto(User user) {
        this(
                user.getId(),
                user.getSerialId(),
                user.getPassword(),
                user.getProvider(),
                user.getRole(),
                user.getCreatedDate(),
                user.getNickname(),
                user.getIsLogin(),
                user.getRefreshToken()
        );
    }
}