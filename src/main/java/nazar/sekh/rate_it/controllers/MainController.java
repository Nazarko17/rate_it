package nazar.sekh.rate_it.controllers;

import nazar.sekh.rate_it.dao.PersonDAO;
import nazar.sekh.rate_it.models.Person;
import nazar.sekh.rate_it.models.Ticket;
import nazar.sekh.rate_it.models.Title;
import nazar.sekh.rate_it.models.User;
import nazar.sekh.rate_it.services.TicketService;
import nazar.sekh.rate_it.services.TitleService;
import nazar.sekh.rate_it.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MainController {
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

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/movies")
    public String movies() {
        return "movies";
    }

    @GetMapping("/tv-shows")
    public String tvShows() {
        return "tv-shows";
    }

    @GetMapping("/games")
    public String games() {
        return "games";
    }

    @GetMapping("/books")
    public String books() { return "books"; }

    @PostMapping("/rate")
    public String rate(@RequestParam int rt) {
        System.out.println(rt);

        return "books";
    }

    @GetMapping("/help-center")
    public String helpCenter(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        if(!isAuthenticated()) { return "redirect:/login"; }
        model.addAttribute("uName", currentUser.getUsername());
        return "help-center";
    }

    @PostMapping("/send-ticket")
    public String requestHelpCenter(Model model, @RequestParam String username, @RequestParam String subject, @RequestParam String text) {
        User user = userService.loadByUsername(username);
        List<Ticket> tickets = user.getTickets();
        Ticket ticket = new Ticket(subject, text, false, user);
        ticketService.save(ticket);
        tickets.add(ticket);
        userService.save(user);
//        model.addAttribute("message", "Your message has been successfully sent to administrators!");
        return "redirect:/help-center";
    }

    @GetMapping({"/index", "/home", "/main"})
    public String indexRedirect() {
        return "redirect:/";
    }

    @GetMapping("/films")
    public String moviesRedirect() {
        return "redirect:/movies";
    }

    @GetMapping({"/tv-series", "/serials"})
    public String tvShowsRedirect() {
        return "redirect:/tv-shows";
    }

    @GetMapping({"/help", "/about", "/about-us", "/contact-us"})
    public String helpCenterRedirect() {
        return "redirect:/help-center";
    }

    @GetMapping("/title/{id}")
    public String oneMovie(@PathVariable int id, Model model) {
        Title title = titleService.findById(id);
        model.addAttribute("title", title);
        Set<Person> cast = title.getCast();
        model.addAttribute("cast", cast.stream().sorted(Comparator.comparingInt(Person::getId)).collect(Collectors.toList())); // nada rishaty
        return "movie";
    }

    @GetMapping("/user/{userName}")
    public String user(@PathVariable("userName") String username, Model model) {
        User user = userService.loadByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("unTitle", user.getUsername());
        return "user-profile";
    }

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        if (isAuthenticated()) {
            model.addAttribute("user", userService.loadByUsername(currentUser.getUsername()));
            model.addAttribute("unTitle", currentUser.getUsername());
            return "user-profile";
        }
        return "redirect:/";
    }

    @GetMapping("/ratings")
    public String ratings() {
        return "ratings";
    }

    @GetMapping("/watchlist")
    public String watchList(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        if(!isAuthenticated()) { return "redirect:/login"; }
        User user = userService.loadByUsername(currentUser.getUsername());
        model.addAttribute("titles", user.getWatchlist());
        model.addAttribute("currentUser", user);
        model.addAttribute("countWatchlist", userService.countWatchlist(user));
        return "watchlist";
    }

    @GetMapping("/add-to-watchlist-{titleId}")
    public String addToWatchList(@PathVariable int titleId, @AuthenticationPrincipal UserDetails currentUser) {
        if (isAuthenticated()){
            User user = userService.loadByUsername(currentUser.getUsername());
            List<Title> watchlist = user.getWatchlist();
            watchlist.add(titleService.findById(titleId));
            userService.save(user);
            return "redirect:/title/" + titleId;
        }
        return "redirect:/login";
    }

    @GetMapping("/settings")
    public String userSettingsGet(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        if(!isAuthenticated()) { return "redirect:/login"; }
        User user = userService.loadByUsername(currentUser.getUsername());
        model.addAttribute("currentUser", user);
        return "settings";
    }

    @PostMapping("/settings")
    public String userSettingsPost(@RequestParam int id, @RequestParam String username,
                                   @RequestParam String email, @RequestParam String password,
                                   @AuthenticationPrincipal UserDetails currentUser) {
        if(!isAuthenticated()) { return "redirect:/login"; }
        User user = userService.loadByUsername(currentUser.getUsername());
        userService.update(id, username, email, password, user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        userDetails.setUsername(username);
        userDetails.setPassword(password);
        return "redirect:/settings";
    }
}
