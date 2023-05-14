package nazar.sekh.rate_it.services;

import nazar.sekh.rate_it.dao.VerificationTokenDAO;
import nazar.sekh.rate_it.models.User;
import nazar.sekh.rate_it.models.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenDAO verificationTokenDAO;

    public VerificationToken findByToken(String token) {
        return verificationTokenDAO.findByToken(token);
    }

    public VerificationToken findByUser(User user) {
        return verificationTokenDAO.findByUser(user);
    }

    public void save(User user, String token){
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationToken.setExpireDate(calculateExpDate(24*60));
        verificationTokenDAO.save(verificationToken);
    }

    public void delete(VerificationToken verificationToken){
        verificationTokenDAO.delete(verificationToken);
    }

    private Timestamp calculateExpDate(int expTime) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expTime);
        return new Timestamp(cal.getTime().getTime());
    }
}
