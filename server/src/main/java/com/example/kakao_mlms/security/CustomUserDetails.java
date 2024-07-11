package com.example.kakao_mlms.security;

import com.example.kakao_mlms.domain.type.ERole;
import com.example.kakao_mlms.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@Setter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomUserDetails implements OAuth2User, OidcUser, UserDetails {
    private final Long id;
    private final String password;
    //private final EProvider provider;
    private final ERole role;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes; //소셜에서 추가

    public static CustomUserDetails create(UserRepository.UserSecurityForm user, Map<String, Object> attributes) {
        CustomUserDetails userPrincipal = create(user);
        userPrincipal.setAttributes(attributes);

        return userPrincipal;
    }

    public static CustomUserDetails create(UserRepository.UserSecurityForm user) {

        return new CustomUserDetails(
                user.getId(),
                user.getPassword(),
                //user.getProvider(),
                user.getRole(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getRoleCode())
                ));
    }
    /*
     * OAuth2User
     */

    @Override
    public String getName() {
        return id.toString();
    }

    /*
     * OidcUser
     */
    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
    //

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    //UserDetails
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
