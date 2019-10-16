package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        List<Meal> authMeal = service.getAll().stream()
                .filter(v -> v.getUserId() == SecurityUtil.authUserId())
                .collect(Collectors.toList());
        ValidationUtil.checkNotFound(!authMeal.isEmpty(), "meals with your id!");
        return MealsUtil.getTos(authMeal, SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAllByDateAndTime(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getAllByDateAndTime");
        List<Meal> authMeal = service.getAll().stream()
                .filter(v -> v.getUserId() == SecurityUtil.authUserId())
                .collect(Collectors.toList());
        ValidationUtil.checkNotFound(!authMeal.isEmpty(), "Your meals haven't found!");
        if (startDate == null) {
            startDate = LocalDate.MIN;
        }
        if (startTime == null) {
            startTime = LocalTime.MIN;
        }
        if (endDate == null) {
            endDate = LocalDate.MAX;
        }
        if (endTime == null) {
            endTime = LocalTime.MAX;
        }
        return MealsUtil.getFilteredTos(authMeal, SecurityUtil.authUserCaloriesPerDay(), LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime));
    }

    public Meal get(int id) {
        log.info("get {}", id);
        Meal meal = service.get(id);
        ValidationUtil.checkNotFound(meal.getUserId() == SecurityUtil.authUserId(), "id=" + id);
        return meal;
    }

    public void delete(int id) {
        log.info("delete {}", id);
        ValidationUtil.checkNotFound(get(id), "id=" + id);
        service.delete(id);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);

        ValidationUtil.checkNotFound(meal.getDateTime() != null, "dateTime=" + meal.getDateTime());
        Meal obj = new Meal(meal.getDateTime(), meal.getDescription(), meal.getCalories(), SecurityUtil.authUserId());
        ValidationUtil.checkNew(obj);
        return service.create(obj);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);

        Meal source = get(id);
        LocalDateTime dateTime;
        String description;
        int calories;
        if (meal.getDateTime() == null) {
            dateTime = source.getDateTime();
        } else {
            dateTime = meal.getDateTime();
        }
        if (meal.getDescription() == null) {
            description = source.getDescription();
        } else {
            description = meal.getDescription();
        }
        if (meal.getCalories() < 0) {
            calories = source.getCalories();
        } else {
            calories = meal.getCalories();
        }

        Meal obj = new Meal(id, dateTime, description, calories, SecurityUtil.authUserId());
        service.update(obj);

    }
}