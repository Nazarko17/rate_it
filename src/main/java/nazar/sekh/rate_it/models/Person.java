package nazar.sekh.rate_it.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"titles", "roles"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "cast")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String surname;
    String bio;
    String avatar;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cast")
    Set<Title> titles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "person")
    List<CastRole> roles = new ArrayList<>();

    public Person(String name, String surname, String bio) {
        this.name = name;
        this.surname = surname;
        this.bio = bio;
    }
}
