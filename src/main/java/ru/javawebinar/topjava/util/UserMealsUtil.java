package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateAndSumCaloriesMap = mealList.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));

        return mealList.stream()
                .filter(elem -> TimeUtil.isBetween(elem.getDateTime().toLocalTime(), startTime, endTime))
                .map(elem -> new UserMealWithExceed(elem.getDateTime(), elem.getDescription(), elem.getCalories(), dateAndSumCaloriesMap.get(elem.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateAndSumCaloriesMap = new HashMap<>();
        Map<UserMeal, LocalDate> userMealAndDateMap = new HashMap<>();
        for (UserMeal userMeal : mealList) {
                dateAndSumCaloriesMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), (k, v) -> v + dateAndSumCaloriesMap.get(userMeal.getDateTime().toLocalDate()));
                userMealAndDateMap.put(userMeal, userMeal.getDateTime().toLocalDate());
        }

        List<UserMealWithExceed> userMealWithExceedFilteredList = new ArrayList<>();
        for (Map.Entry<UserMeal, LocalDate> pair : userMealAndDateMap.entrySet()) {
            if (TimeUtil.isBetween(pair.getKey().getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExceedFilteredList.add(new UserMealWithExceed(pair.getKey().getDateTime(), pair.getKey().getDescription(), pair.getKey().getCalories(), dateAndSumCaloriesMap.get(pair.getValue()) > caloriesPerDay));
            }
        }
        return userMealWithExceedFilteredList;
    }
}