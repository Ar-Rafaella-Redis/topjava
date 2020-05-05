package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
//    @Query(name = User.DELETE)
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id =:user_id")
    int delete(@Param("id") int id, @Param("user_id") int user_id);

    /*
    @Modifying
    @Query("SELECT Meal FROM Meal m WHERE m.id=:id AND m.user.id =:user_id ORDER BY m.dateTime DESC")
    List<Meal> findAll(@Param("id") int id, @Param("user_id") int user_id);
    */

    List<Meal> findMealByDateTimeBetweenAndUser(@Nullable LocalDateTime dateTime, @Nullable LocalDateTime dateTime2, @NotNull User user, @Nullable Sort sortDateStart);

    List<Meal> queryAllByDateTimeIsAfterAndDateTimeBeforeAndUser(@Nullable LocalDateTime dateTime, @Nullable LocalDateTime dateTime2, @NotNull User user, @Nullable Sort sortDateStart);

    List<Meal> findAllByUser(User one, Sort sortDateStart);
}
