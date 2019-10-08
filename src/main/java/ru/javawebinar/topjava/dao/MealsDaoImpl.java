package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsDaoImpl implements CrudDao<Meal> {

    private static AtomicInteger id = new AtomicInteger(6);
    private static List<Meal> meals = new ArrayList<>(Arrays.asList(
            new Meal(0, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new Meal(1, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(2, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(3, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new Meal(4, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
            new Meal(5, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
    ));

    @Override
    public void create(Meal meal) {
        if (meal != null && meal.getDescription() != null && meal.getDateTime() != null
                && meal.getCalories() != null && meal.getCalories() >= 0) {
            meal.setId(id.getAndIncrement());
            meals.add(meal);
        }
    }

    @Override
    public Optional<Meal> find(Integer id) {
        if (id == null)
            return Optional.empty();

        for (Meal meal : meals) {
            if (meal.getId().equals(id)) {
                return Optional.of(meal);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Meal> findAll() {
        return meals;
    }

    @Override
    public void update(Meal meal) {
        if (meal == null) {
            return;
        }
        AtomicInteger i = new AtomicInteger(0);
        for (; i.get() < meals.size(); i.getAndIncrement()) {
            if (meals.get(i.get()).getId().equals(meal.getId())) {
                break;
            }
        }

        LocalDateTime dateTime;
        String description;
        Integer calories;

        if (meal.getDateTime() == null) {
            dateTime = meals.get(i.get()).getDateTime();
        } else {
            dateTime = meal.getDateTime();
        }

        if (meal.getDescription() == null || meal.getDescription().isEmpty()) {
            description = meals.get(i.get()).getDescription();
        } else {
            description = meal.getDescription();
        }

        if (meal.getCalories() == null || meal.getCalories() < 0) {
            calories = meals.get(i.get()).getCalories();
        } else {
            calories = meal.getCalories();
        }

        meals.set(i.get(), new Meal(meal.getId(), dateTime, description, calories));
    }

    @Override
    public void delete(Integer id) {
        if (id != null) {
            for (Meal meal : meals) {
                if (meal.getId().equals(id)) {
                    meals.remove(meal);
                    return;
                }
            }
        }
    }
}
