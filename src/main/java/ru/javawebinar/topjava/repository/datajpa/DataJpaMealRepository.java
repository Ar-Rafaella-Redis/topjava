package ru.javawebinar.topjava.repository.datajpa;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_DATE_START = Sort.by(Sort.Direction.DESC, "dateTime");
    private static final Logger log = getLogger("result");
    @Autowired
    private CrudMealRepository crudRepository;
    @Autowired
    private CrudUserRepository crudUserRepository;

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("*******************DATAJPA%%%%%%%%%%%%%");
        meal.setUser(crudUserRepository.getOne(userId));
        if (!meal.isNew() && get(meal.getId(), userId) == null)
            return null;
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
       return crudRepository.delete(id,userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
       Meal meal = crudRepository.findById(id).orElse(null);
        return meal != null &&  meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
         return crudRepository.findAllByUser(crudUserRepository.getOne(userId), SORT_DATE_START);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        User user = crudUserRepository.getOne(userId);
        return crudRepository.queryAllByDateTimeIsAfterAndDateTimeBeforeAndUser(startDateTime, endDateTime, user, SORT_DATE_START);
    }
}
