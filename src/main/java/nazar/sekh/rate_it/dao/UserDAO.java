package nazar.sekh.rate_it.dao;

import nazar.sekh.rate_it.models.Title;
import nazar.sekh.rate_it.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.id=:id")
    User findUserById(int id);

    @Query("select u from User u where u.username=:username")
    User loadUserByUsername(String username);

    @Query("select u from User u where u.email=:email")
    User findByEmail(String email);

    @Query("select u from User u where u.username like %:keyword%")
    List<User> findUserByKeyword(String keyword);
}
