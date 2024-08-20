package com.example.kakao_mlms.repository;

import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.EProvider;
import com.example.kakao_mlms.domain.type.ERole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayName("JPA CRUD TEST")
@DataJpaTest()
class JpaRepositoryTest {
    private final UserRepository userRepository;

    public JpaRepositoryTest(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void select() {
        // Given

        // When
        List<User> users = userRepository.findAll();

        // Then
        assertThat(users).isNotNull().hasSize(5);

    }

    @Test
    void insert() {
        // Given
        long prevCount = userRepository.count();
        User user = User.builder()
                .serialId("social_id_6")
                .password("password6")
                .provider(EProvider.DEFAULT)
                .role(ERole.ADMIN)
                .nickname("nickname6")
                .build();

        // When
        userRepository.save(user);

        // Then
        assertThat(userRepository.count()).isEqualTo(prevCount + 1);

    }

    @Test
    void update() {
        // Given
        User user = userRepository.findById(1L).orElseThrow();
        user.updateInfo("nickname7");

        // When
        User savedUser = userRepository.saveAndFlush(user);

        // Then
        assertThat(savedUser).hasFieldOrPropertyWithValue("nickname", "nickname7");

    }

    @Test
    void delete() {
        // Given
        User user = userRepository.findById(1L).orElseThrow();
        long prevCount = userRepository.count();

        // When
        userRepository.delete(user);

        // Then
        assertThat(userRepository.count()).isEqualTo(prevCount - 1);
    }
}