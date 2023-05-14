package nazar.sekh.rate_it.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Enumerated(EnumType.STRING)
    CustomRole customRole = CustomRole.ROLE_USER;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    User user;

    public Role(CustomRole customRole, User user) {
        this.customRole = customRole;
        this.user = user;
    }
}
