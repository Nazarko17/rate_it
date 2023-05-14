package nazar.sekh.rate_it.controllers;

import nazar.sekh.rate_it.dao.PersonDAO;
import nazar.sekh.rate_it.models.CastRole;
import nazar.sekh.rate_it.models.Person;
import nazar.sekh.rate_it.models.Title;
import nazar.sekh.rate_it.services.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
public class CustomRestController {
    @Autowired
    private TitleService titleService;

    @Autowired
    private PersonDAO personDAO;

//    ------------------------------ADMIN------------------------------
    @PostMapping("/admin/add-person")
    public void adminAddCast(@RequestParam String name,
                          @RequestParam String surname,
                          @RequestParam String bio,
                          @RequestParam("picture") MultipartFile multipartFile) {

        Person person = new Person(name, surname, bio);
        titleService.transferFile(multipartFile);
        person.setAvatar(multipartFile.getOriginalFilename());
        personDAO.save(person);
    }

    @PostMapping("/admin/add-roles-to-person")
    public void adminAddRolesToPerson(@RequestParam int personId,
                                      @RequestParam int movieId,
                                      @RequestParam String role) {
        Title title = titleService.findById(movieId);
        Set<Person> cast = title.getCast();
        Person person = personDAO.findPersonById(personId);
        List<CastRole> roles = person.getRoles();
        CastRole castRole = new CastRole(role, movieId, personDAO.findPersonById(personId));
        roles.add(castRole);
        personDAO.save(person);
        cast.add(person);
        titleService.saveTitle(title);
    }

    @PostMapping("/admin/add-movie")
    public String addMovie(@RequestParam String name,
                           @RequestParam String storyline,
                           @RequestParam int releaseYear,
                           @RequestParam int runTime,
                           @RequestParam String mmpa,
                           @RequestParam int budget,
                           @RequestParam int boxOffice,
                           @RequestParam String trailerURL,
                           @RequestParam("picture") MultipartFile multipartFile) {
        Title title = new Title(name, storyline, releaseYear, runTime, mmpa, budget, boxOffice, trailerURL);
        titleService.transferFile(multipartFile);
        title.setAvatar(multipartFile.getOriginalFilename());
        titleService.saveTitle(title);
        System.out.printf(String.valueOf(title));
        return "admin/add-movie";
    }
}
