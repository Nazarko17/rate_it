package nazar.sekh.rate_it.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "userT")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String subject;
    String text;
    boolean status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    User userT;

    public Ticket(String subject, String text, boolean status, User userT) {
        this.subject = subject;
        this.text = text;
        this.status = status;
        this.userT = userT;
    }
}
