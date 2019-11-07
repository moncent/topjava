package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_DATETIME_CALORIES = Sort.by(Sort.Direction.DESC, "date_time", "calories");

    @Autowired
    private CrudMealRepository crudRepository;

    @Autowired
    private CrudUserRepository userRepository;

    @Override
    public Meal save(Meal meal, int userId) {
        User user = userRepository.getOne(userId);
        if (user == null) {
            return null;
        }
        meal.setUser(user);
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.deleteById(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getOne(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAll(userId, Sort.by(Sort.Direction.DESC, "date_time"));
    }

    @Override
    public List<Meal> getBetweenInclusive(LocalDate startDate, LocalDate endDate, int userId) {
        return crudRepository.findAll(startDate, endDate, userId, SORT_DATETIME_CALORIES);
    }
}
