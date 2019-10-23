package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.Util;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@Repository
public class InMemoryMealRepository extends InMemoryBaseRepository<Meal> implements MealRepository {

    private Map<Integer, Integer> mealAndUserLink = new ConcurrentHashMap<>();

    public void init() {
        map.clear();
        mealAndUserLink.clear();
        map.put(MEAL_ID1, meal1);
        map.put(MEAL_ID2, meal2);
        map.put(MEAL_ID3, meal3);
        map.put(MEAL_ID4, meal4);
        map.put(MEAL_ID5, meal5);
        mealAndUserLink.put(MEAL_ID1, USER_ID);
        mealAndUserLink.put(MEAL_ID2, USER_ID);
        mealAndUserLink.put(MEAL_ID3, ADMIN_ID);
        mealAndUserLink.put(MEAL_ID4, USER_ID);
        mealAndUserLink.put(MEAL_ID5, ADMIN_ID);
    }


    @Override
    public Meal save(Meal meal, int userId) {
        Meal newMeal = save(meal);
        mealAndUserLink.put(newMeal.getId(), userId);
        return newMeal;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (mealAndUserLink.get(id) == userId) {
            mealAndUserLink.remove(id);
            return delete(id);
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = getAll(userId);
        return getAll(userId) == null ? null : meals.stream()
                                                .filter(meal -> meal.getId() == id)
                                                .findFirst()
                                                .orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return getAllFiltered(userId, meal -> Util.isBetweenInclusive(meal.getDateTime(), startDate, endDate));
    }

    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
      return getCollection().stream()
                        .filter(meal -> mealAndUserLink.get(meal.getId()) == userId)
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }
}