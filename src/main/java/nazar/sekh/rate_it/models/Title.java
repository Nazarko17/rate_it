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
@ToString(exclude = {"cast"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "titles")
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String storyline;
    int releaseYear;
    String avatar;
    int rating;
    int runTime;
    String mmpa;
    double budget;
    double boxOffice;
    String trailerURL;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<Person> cast = new HashSet<>();

    public Title(String name, String storyline, int releaseYear, int runTime, String mmpa, double budget, double boxOffice, String trailerURL) {
        this.name = name;
        this.storyline = storyline;
        this.releaseYear = releaseYear;
        this.runTime = runTime;
        this.mmpa = mmpa;
        this.budget = budget;
        this.boxOffice = boxOffice;
        this.trailerURL = trailerURL;
    }
}
