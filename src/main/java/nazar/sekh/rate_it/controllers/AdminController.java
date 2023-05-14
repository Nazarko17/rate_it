package nazar.sekh.rate_it.controllers;

import nazar.sekh.rate_it.dao.PersonDAO;
import nazar.sekh.rate_it.models.*;
import nazar.sekh.rate_it.services.EmailService;
import nazar.sekh.rate_it.services.TicketService;
import nazar.sekh.rate_it.services.TitleService;
import nazar.sekh.rate_it.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.Cast;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class AdminController {
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
    private TitleService titleService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EmailService emailService;

    @RequestMapping("/access-denied")
    public String error() {
        return "redirect:/";
    }

    @GetMapping("/admin")
    public String admin() {
        if (isAuthenticated()) {
            return "redirect:/admin/users";
        }
        return "redirect:/";
    }

    @GetMapping("/admin/users")
    public String adminUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/admin/user-delete/{id}")
    public String adminUserDelete(@PathVariable("id") int id) {
        userService.delete(userService.findByID(id));
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/{userName}")
    public String adminSingleUser(@PathVariable("userName") String username, Model model) {
        User user = userService.loadByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("unTitle", user.getUsername());
        return "admin/user-profile";
    }

    @GetMapping("/admin/movies-and-shows")
    public String adminMovies(Model model) {
        model.addAttribute("movies", titleService.findAll());
        return "admin/movies";
    }

    @GetMapping("/admin/add-movie")
    public String addMovie(Model model) {
        model.addAttribute("title", new Title());
        return "admin/add-movie";
    }

    @GetMapping("/admin/edit-movie/{id}")
    public String editMovie(@PathVariable int id, Model model) {
        model.addAttribute("title", titleService.findById(id));
        return "admin/add-movie";
    }

    @GetMapping("/admin/cast")
    public String adminCast(Model model) {
        model.addAttribute("cast", personDAO.findAll());
        return "admin/cast";
    }

    @GetMapping("/admin/add-person")
    public String addCast(Model model) {
        return "admin/add-person";
    }
    @PostMapping("/admin/cast-search")
    public <T>String adminSearchCast(Model model, @RequestParam T keyword) {
        model.addAttribute("cast", personDAO.findByKeyword(keyword));
        return "admin/cast";
    }
    @Autowired
    PersonDAO personDAO;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/admin/users-search")
    public String adminUsersMovies(Model model, @RequestParam String keyword) {
        model.addAttribute("users", userService.findUserByKeyword(keyword));
        return "admin/users";
    }

    @PostMapping("/admin/movies-and-shows-search")
    public <T>String adminSearchMovies(Model model, @RequestParam T keyword) {
        model.addAttribute("movies", titleService.findByKeyword(keyword));
        return "admin/movies";
    }

    @GetMapping("/admin/tickets")
    public String tickets(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "admin/tickets";
    }

    @GetMapping("/admin/answer-ticket/{id}")
    public String answerTicketGet(Model model, @PathVariable int id) {
        Ticket ticket = ticketService.findById(id);
        model.addAttribute("ticket", ticket);
        return "admin/answer-ticket";
    }

    @PostMapping("/admin/answer-ticket")
    public String answerTicketPost(@RequestParam int id, @RequestParam String answer) throws MessagingException {
        Ticket ticket = ticketService.findById(id);
        emailService.answerTicket(ticket.getUserT().getUsername(), ticket.getUserT().getEmail(), ticket.getSubject(),
                ticket.getText(), answer);
        return "redirect:/admin/tickets";
    }

    @GetMapping ("/admin/close-ticket/{id}")
    public String closeTicket(@PathVariable int id) {
        ticketService.findById(id).setStatus(true);
        ticketService.save(ticketService.findById(id));
        return "redirect:/admin/tickets";
    }
}
