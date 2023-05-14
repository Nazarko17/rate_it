package nazar.sekh.rate_it.dao;

import nazar.sekh.rate_it.models.Person;
import nazar.sekh.rate_it.models.Title;
import nazar.sekh.rate_it.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonDAO extends JpaRepository<Person, Integer> {
    @Query("select p from Person p where p.id=:id")
    Person findPersonById(int id);

    @Query("select p from Person p where p.name like %:keyword% or p.surname like %:keyword%")
    <T>List<Person> findByKeyword(T keyword);
}
