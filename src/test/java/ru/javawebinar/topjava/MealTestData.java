package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryBaseRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    private static Map<Integer, InMemoryBaseRepository<Meal>> usersMealsMap = new ConcurrentHashMap<>();

    public static final Meal ADMIN_SUPPER = new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);
    public static final Meal ADMIN_LUNCH = new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);

    public static final Meal USER_DINNER = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    private static AtomicInteger atomicInt = new AtomicInteger(100000);
    public static void initMealData() {
       usersMealsMap.clear();
        atomicInt.set(START_SEQ);
        MealsUtil.MEALS.forEach(meal -> save(meal, USER_ID));
       // save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510), ADMIN_ID);
       save(USER_DINNER,USER_ID);
       save(ADMIN_LUNCH,ADMIN_ID);
       save(ADMIN_SUPPER,ADMIN_ID);
    }

    public static Meal save(Meal meal, int userId) {
        meal.setId(atomicInt.incrementAndGet());
        if (! meal.equals(USER_DINNER)) {
            InMemoryBaseRepository<Meal> meals = usersMealsMap.computeIfAbsent(userId, uid -> new InMemoryBaseRepository<>());
            return meals.save(meal);
        }
         return null;
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }

   public static Meal getUpdatedMeal(Meal meal){
        Meal updated = new Meal();
        updated.setId(meal.getId());
        updated.setDescription(meal.getDescription());
        updated.setDateTime(LocalDateTime.of(2015, Month.JUNE, 7, 14, 0));
        updated.setCalories(780);
        return updated;
   }

    public static Meal getNewMeal(){
        return new Meal(LocalDateTime.of(2020, Month.MARCH, 17, 15, 10), "Админ обед", 510);
    }
}


