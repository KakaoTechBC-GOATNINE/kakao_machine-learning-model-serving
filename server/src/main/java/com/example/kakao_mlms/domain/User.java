package com.example.kakao_mlms.domain;

import com.example.kakao_mlms.domain.type.EProvider;
import com.example.kakao_mlms.domain.type.ERole;
import com.example.kakao_mlms.dto.request.AuthSignUpDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
@Entity
@Getter
@DynamicUpdate
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "social_id", nullable = false, unique = true)
    private String serialId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private EProvider provider;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private ERole role;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "nickname", length = 12)
    private String nickname;

    /* User Status */
    @Column(name = "is_login", nullable = false)
    private Boolean isLogin;

    @Column(name = "refresh_token")
    private String refreshToken;

    /* Relation Parent Mapping */
    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Qna> qnaList = new ArrayList<>();

    @Builder
    public User(Long id, String serialId, String password,  String nickname, EProvider provider, ERole role) {
        this.id = id;
        this.serialId = serialId;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.role = role;
        this.createdDate = LocalDate.now();
        this.isLogin = false;
    }

    @Builder
    public User(String serialId, String password, EProvider provider, ERole role) {
        this.serialId = serialId;
        this.password = password;
        this.provider = provider;
        this.role = role;
        this.createdDate = LocalDate.now();
        this.isLogin = true;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateInfo(String nickname, String phoneNumber) {
        if (nickname != null && (!Objects.equals(this.nickname, nickname))) {
            this.nickname = nickname;
        }
    }

    public static User signUp(AuthSignUpDto authSignUpDto, String encodedPassword) {
        User user = User.builder()
                .serialId(authSignUpDto.serialId())
                .password(encodedPassword)
                .provider(EProvider.DEFAULT)
                .role(ERole.USER)
                .build();
        user.register(authSignUpDto.nickname());

        return user;
    }

    public void register(String nickname) {
        this.nickname = nickname;
        this.role = ERole.USER;
    }
}
