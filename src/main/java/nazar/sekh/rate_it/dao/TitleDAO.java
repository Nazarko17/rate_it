package nazar.sekh.rate_it.dao;

import nazar.sekh.rate_it.models.Title;
import nazar.sekh.rate_it.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TitleDAO extends JpaRepository<Title, Integer> {
    @Query("select t from Title t where t.id=:id")
    Title findTitleById(int id);

    @Query("select t from Title t where t.releaseYear like %:keyword% or t.name like %:keyword%")
    <T>List<Title> findByKeyword(T keyword);


}
