package nazar.sekh.rate_it.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"person"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CastRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String role;
    int movieId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Person person;

    public CastRole(String role, int movieId, Person person) {
        this.role = role;
        this.movieId = movieId;
        this.person = person;
    }
}
