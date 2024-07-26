package com.example.kakao_mlms.service;

import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.ERole;
import com.example.kakao_mlms.dto.response.JwtTokenDto;
import com.example.kakao_mlms.exception.CommonException;
import com.example.kakao_mlms.exception.ErrorCode;
import com.example.kakao_mlms.repository.QnaRepository;
import com.example.kakao_mlms.repository.UserRepository;
import com.example.kakao_mlms.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final QnaRepository qnaRepository;

    @Transactional
    public JwtTokenDto reissue(final String refreshToken) {
        return jwtUtil.reissue(refreshToken);
    }

    public Boolean withdrawUser(Long id, Long userId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        if (!(id == userId || user.getRole() == ERole.ADMIN))
            throw new CommonException(ErrorCode.ACCESS_DENIED_ERROR);
        //Qna는 삭제하지 않고 기본 유저 id로 전환
        qnaRepository.updateQnaByUserId(userId);

        userRepository.delete(user);
        return Boolean.TRUE;
    }
}
