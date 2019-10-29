package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User u = em.getReference(User.class, userId);
            meal.setUser(u);
            em.persist(meal);
        } else if (em.createNamedQuery(Meal.UPDATE)
                     .setParameter("description", meal.getDescription())
                     .setParameter("calories", meal.getCalories())
                     .setParameter("dateTime", meal.getDateTime())
                     .setParameter("id", meal.getId())
                     .setParameter("userId", userId)
                     .executeUpdate() == 0) {
            return null;
        }
        return meal;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                 .setParameter("userId", userId)
                 .setParameter("id", id)
                 .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return em.createNamedQuery(Meal.GET, Meal.class)
                .setParameter("userId", userId)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(Meal.ALL_WITH_TIME_SORTED, Meal.class)
                 .setParameter("userId", userId)
                 .setParameter("startDateTime", startDate)
                 .setParameter("endDateTime", endDate)
                 .getResultList();
    }
}