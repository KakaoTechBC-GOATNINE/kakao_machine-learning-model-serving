package com.example.kakao_mlms.service;

import com.example.kakao_mlms.dto.response.UserDto;
import com.example.kakao_mlms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDto::new);
    }

    public Page<UserDto> getUsersByName(String name, Pageable pageable) {
        return userRepository.findByNickname(name, pageable).map(UserDto::new);
    }
}
