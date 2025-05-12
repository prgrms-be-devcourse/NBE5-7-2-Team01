package com.fifo.ticketing.domain.user.dto.oauth;

import com.fifo.ticketing.domain.user.entity.Role;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Accessors(chain = true)
public class UserOAuthDetails implements OAuth2User {

    private final String name;
    private final String email;
    private final Map<String, Object> attributes;

    @Setter
    private Role role;

    @Builder
    public UserOAuthDetails(String name, String email, Map<String, Object> attributes, Role role) {
        this.name = name;
        this.email = email;
        this.attributes = attributes;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
