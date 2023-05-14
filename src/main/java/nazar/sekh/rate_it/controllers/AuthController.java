package nazar.sekh.rate_it.controllers;

import nazar.sekh.rate_it.models.User;
import nazar.sekh.rate_it.models.VerificationToken;
import nazar.sekh.rate_it.services.EmailService;
import nazar.sekh.rate_it.services.UserService;
import nazar.sekh.rate_it.services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.UUID;

@Controller
public class AuthController {
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @GetMapping("/login")
    public String login() {
        if(isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/forgotten-pass")
    public String forgottenPassword() {
        return "forgotten-pass";
    }

    @PostMapping("/forgotten-pass")
    public String forgottenPassword(Model model, @RequestParam String email) throws MessagingException {
        if(isAuthenticated()) {
            return "redirect:/";
        }
        if (userService.findByEmail(email) != null) {
            User user = userService.findByEmail(email);
            userService.fogottenPass(user);
            model.addAttribute("message", "An email has been sent to you!");
        }
        return "forgotten-pass";
    }

    @GetMapping("/password-reset-{token}")
    public String passReset(@PathVariable("token") String token, Model model) {
        if(isAuthenticated()) {
            return "redirect:/";
        }
        VerificationToken byToken = verificationTokenService.findByToken(token);
        model.addAttribute("user", byToken.getUser());
        return "new-pass";
    }

    @PostMapping("/password-reset")
    public String passReset(@RequestParam String password, @RequestParam int id) {
        if(isAuthenticated()) {
            return "redirect:/";
        }
        User user = userService.findByID(id);
        user.setPassword(password);
        userService.update(id, user.getUsername(), user.getEmail(), password, user);
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        if(isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute(new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid User user, BindingResult bindingResult, @RequestParam("picture") MultipartFile multipartFile, Model model) throws MessagingException {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        model.addAttribute("message","An activation email has been sent to you!");
        userService.transferFile(multipartFile);
        user.setAvatar(multipartFile.getOriginalFilename());
        userService.register(user);
        System.out.printf(String.valueOf(user));
        return "register";
    }

    @GetMapping("/confirm-{token}")
    public String regSuccess(@PathVariable("token") String token, Model model) {
        if(token == null) {
            model.addAttribute("message", "Your verification token is invalid!");
        } else {
            VerificationToken byToken = verificationTokenService.findByToken(token);
            User user = byToken.getUser();
            if(!user.isEnabled()) {
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                if(byToken.getExpireDate().before(currentTimestamp)) {
                    model.addAttribute("message","Your verification token has expired!");
                } else {
                    userService.enableAccount(token);
                    model.addAttribute("message","Your account is successfully activated!");
                }
            } else {
                model.addAttribute("message","Your account is already activated!");
            }
        }
        return "login";
    }
}
