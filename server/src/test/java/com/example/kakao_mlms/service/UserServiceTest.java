package com.example.kakao_mlms.service;

import com.example.kakao_mlms.dto.UserDto;
import com.example.kakao_mlms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks private UserService sut;
    @Mock private UserRepository userRepository;

    @Test
    void searchUsers() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(userRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<UserDto> userDtos = sut.searchUsers(null, pageable);

        // Then
        assertThat(userDtos).isEmpty();
        then(userRepository).should().findAll(pageable);

    }

    @Test
    void searchUsersByNickname() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        String nickname = "nickname";
        given(userRepository.findByNicknameContaining(nickname, pageable)).willReturn(Page.empty());

        // When
        Page<UserDto> userDtos = sut.searchUsers(nickname, pageable);

        // Then
        assertThat(userDtos).isEmpty();
        then(userRepository).should().findByNicknameContaining(nickname, pageable);
    }
}