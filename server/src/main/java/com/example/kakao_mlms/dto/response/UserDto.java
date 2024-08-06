package com.example.kakao_mlms.dto.response;

import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.EProvider;
import com.example.kakao_mlms.domain.type.ERole;
import lombok.Builder;

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
) {
    public static UserDto from(User user) {
        return new UserDto(
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

    public static UserDto of(String serialId, ERole role) {
        return new UserDto(1L, serialId, null, EProvider.KAKAO, role, LocalDate.now(), null, false, null);
    }

    public User toEntity() {
        return User.builder().id(id).serialId(serialId).password(password).nickname(nickname).provider(provider).role(role).build();
    }
}