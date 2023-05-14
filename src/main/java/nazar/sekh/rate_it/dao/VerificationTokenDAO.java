package nazar.sekh.rate_it.dao;

import nazar.sekh.rate_it.models.User;
import nazar.sekh.rate_it.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenDAO extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
