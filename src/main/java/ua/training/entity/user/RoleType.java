package ua.training.entity.user;

import org.springframework.security.core.GrantedAuthority;

public enum RoleType implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_SUPERADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
