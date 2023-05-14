package nazar.sekh.rate_it.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"roles", "tickets"})
@FieldDefaults(level = AccessLevel.PRIVATE)
//@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String username;
    String email;
    String password;
    String avatar;
    String createdAt;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    List<Role> roles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Title> watchlist = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userT")
    List<Ticket> tickets = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getCustomRole().toString()));
        }
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    boolean isAccountNonExpired = true;
    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    boolean isAccountNonLocked = true;
    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    boolean isCredentialsNonExpired = true;
    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    boolean isEnabled = false;
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
