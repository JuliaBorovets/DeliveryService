package ua.training.entity.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"login", "email"})})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "first_name_cyr", nullable = false)
    private String firstNameCyr;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "last_name_cyr", nullable = false)
    private String lastNameCyr;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean accountNonExpired;

    @Column(nullable = false)
    private boolean accountNonLocked;

    @Column(nullable = false)
    private boolean credentialsNonExpired;

    @Column(nullable = false)
    private boolean enabled;

    @Column
    private Double balance;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> result = new HashSet<>();
        result.add(getRole());
        return result;
    }

    @Override
    public String getUsername() {
        return getLogin();
    }
}
