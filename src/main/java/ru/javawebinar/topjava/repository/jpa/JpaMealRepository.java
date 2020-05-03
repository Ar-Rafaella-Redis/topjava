package ru.javawebinar.topjava.repository.jpa;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {
/*
    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.getCurrentSession();
    }*/
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        meal.setUser(em.getReference(User.class, userId));
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else if (get(meal.getId(), userId) == null) {
            return null;
        }
        return em.merge(meal);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
       Query query =
               //em.createQuery("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.user.id =:user_id AND m.id=:id",Meal.class);
      em.createNamedQuery(Meal.GET,Meal.class);
      List<Meal> meals =  query.setParameter("id", id).setParameter("user_id", userId).getResultList();
      return meals.isEmpty()? null: DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.GET_ALL,Meal.class).setParameter("user_id", userId).getResultList();
     }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(Meal.GET_BETWEEN,Meal.class).setParameter("user_id", userId)
                .setParameter("start_time", startDate)
                .setParameter("end_time", endDate).getResultList();
    }
}