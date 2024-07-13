package com.example.kakao_mlms.service;

import com.example.kakao_mlms.dto.response.UserDto;
import com.example.kakao_mlms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserDto> searchUsers(String name, Pageable pageable) {
        if(StringUtils.hasText(name)) {
            return userRepository.findByNicknameContaining(name, pageable).map(UserDto::from);
        }
        return userRepository.findAll(pageable).map(UserDto::from);
    }
}
