package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.UserTestData.assertMatch;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    @Autowired
    private MealService service;

    @Before
    public void setUp() throws Exception {
        MealTestData.initMealData();
   }

    @Test
    public void get() {
        Meal meal = service.get(USER_DINNER.getId(),USER_ID);
        meal.equals(USER_DINNER);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(USER_DINNER.getId(),USER_ID);
        service.get(USER_DINNER.getId(),USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteForeignMeal() {
        service.delete(USER_DINNER.getId(),ADMIN_ID);
     }

    @Test
    public void getBetweenHalfOpen() {
       List<Meal> mealsDB = service.getBetweenHalfOpen(LocalDate.of(2015, Month.JUNE, 1),null,ADMIN_ID);
        assertThat(mealsDB).isEqualTo(Arrays.asList(ADMIN_SUPPER,ADMIN_LUNCH));
    }

    @Test
    public void getAll() {
        List<Meal> mealsDB = service.getAll(ADMIN_ID);
        assertThat(mealsDB).isEqualTo(Arrays.asList(ADMIN_SUPPER,ADMIN_LUNCH));
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdatedMeal(ADMIN_LUNCH);
        service.update(updated,ADMIN_ID);
        service.get(ADMIN_LUNCH.getId(),ADMIN_ID).equals(updated);
    }

    @Test
    public void create() {
        Meal newMeal = getNewMeal();
        service.create(newMeal,ADMIN_ID);
        service.get(newMeal.getId(),ADMIN_ID).equals(newMeal);
    }
}