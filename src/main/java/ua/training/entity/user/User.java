package ua.training.entity.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderCheck;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"login", "id"})})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String firstNameCyr;

    private String lastNameCyr;

    private String login;

    private String email;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private RoleType role;

    @OneToMany(mappedBy = "owner")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<OrderCheck> checks = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "user_card",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id"))
    private List<BankCard> cards = new ArrayList<>();

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;


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

    public BankCard addBankCard(BankCard bankCard){
        cards.add(bankCard);
        bankCard.getUsers().add(this);
        return bankCard;
    }



}
