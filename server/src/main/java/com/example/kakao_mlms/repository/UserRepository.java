package com.example.kakao_mlms.repository;

import com.example.kakao_mlms.domain.User;
import com.example.kakao_mlms.domain.type.EProvider;
import com.example.kakao_mlms.domain.type.ERole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying(clearAutomatically = true)
    @Query(value = "update User u set u.refreshToken = :refreshToken where u.id = :userId")
    void updateRefreshToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);

    Page<User> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);

    Optional<User> findByIdAndProvider(Long userId, EProvider provider);

    Optional<UserSecurityForm> findBySerialIdAndProvider(String serialId, EProvider provider);

    @Query(value = "SELECT u.id as id, u.password as password, u.role as role" +
            " FROM User u WHERE u.serialId = :userId")
    Optional<UserSecurityForm> findUserIdAndRoleBySerialId(@Param("userId") String userId);

    Optional<User> findUserBySerialId(String userId);

    Boolean existsByNickname(String nickname);

    Optional<User> findBySerialIdOrNickname(String serialId, String nickname);

    @Modifying(clearAutomatically = true)
    @Query(value = "update User u set u.refreshToken = :refreshToken, u.isLogin = :isLogin where u.id = :userId")
    void updateRefreshTokenAndLoginStatus(@Param("userId") Long userId, @Param("refreshToken") String refreshToken, @Param("isLogin") Boolean isLogin);

    Optional<UserSecurityForm> findByIdAndIsLoginAndRefreshTokenIsNotNull(Long id, boolean b);

    interface UserSecurityForm {
        static UserSecurityForm invoke(User user) {
            return new UserSecurityForm() {
                @Override
                public Long getId() {
                    return user.getId();
                }

                @Override
                public String getPassword() {
                    return user.getPassword();
                }

                @Override
                public ERole getRole() {
                    return user.getRole();
                }
            };
        }

        Long getId();

        String getPassword();

        ERole getRole();
    }

}
