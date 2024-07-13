package com.example.kakao_mlms.service;

import com.example.kakao_mlms.dto.response.UserDto;
import com.example.kakao_mlms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDto::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByName(String name, Pageable pageable) {
        return userRepository.findByNickname(name, pageable).map(UserDto::new);
    }
}
