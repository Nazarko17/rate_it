package nazar.sekh.rate_it.services;

import nazar.sekh.rate_it.dao.TicketDAO;
import nazar.sekh.rate_it.dao.UserDAO;
import nazar.sekh.rate_it.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TicketDAO ticketDAO;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) throws MessagingException {
        //createdAt
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String creadetAt = df.format(Calendar.getInstance().getTime());
        user.setCreatedAt(creadetAt);
        //password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //token
        String token = UUID.randomUUID().toString();
        verificationTokenService.save(user, token);
        //role
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(CustomRole.ROLE_USER, user));
//        roles.add(new Role(CustomRole.ROLE_ADMIN, user));
        user.setRoles(roles);
        //email
        emailService.sendRegMail(user.getUsername(), user.getEmail(), token);
        return userDAO.save(user);
    }

    public User update(int id, String username, String email, String password, User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User newUser = userDAO.findUserById(id);
        if(!password.equals("") && !password.equals(null)) {
            if (!encoder.matches(password, user.getPassword())) {
                newUser.setPassword(passwordEncoder.encode(password));
            }
        }
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setEnabled(true);
        return userDAO.save(newUser);
    }

    public User save(User user) {
        return userDAO.save(user);
    }

    public User enableAccount(String token) {
        VerificationToken byToken = verificationTokenService.findByToken(token);
        User user = byToken.getUser();
        user.setEnabled(true);
        return userDAO.save(user);
    }

    public void transferFile(MultipartFile file) {
        String pathToFolder = System.getProperty("user.home") + File.separator + "avatars" + File.separator;
        try {
            file.transferTo(new File(pathToFolder + file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(User user) {
        VerificationToken byUser = verificationTokenService.findByUser(user);
        verificationTokenService.delete(byUser);
        userDAO.delete(user);
    }
    public void fogottenPass(User user) throws MessagingException {
        String token = UUID.randomUUID().toString();
        verificationTokenService.save(user, token);
        emailService.sendForgPassMail(user.getUsername(), user.getEmail(), token);
    }

    public List<User> findUserByKeyword(String keyword) {
        return userDAO.findUserByKeyword(keyword);
    }

    public User loadByUsername(String username) {
        return userDAO.loadUserByUsername(username);
    }

    public User findByID(int id) {
        return userDAO.findUserById(id);
    }

    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public int countWatchlist(User user) {
        List<Title> watchlist = user.getWatchlist();
        int count = 0;
        for (Title title : watchlist) {
            ++count;
        }
        return count;
    }
}
