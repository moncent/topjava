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
        Map<LocalDate, IntSummaryStatistics> map = mealList.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), Collectors.summarizingInt(UserMeal::getCalories)));

        List<UserMealWithExceed> list = new ArrayList<>();
        for (UserMeal userMeal : mealList) {
            if (map.get(userMeal.getDateTime().toLocalDate()).getSum() > caloriesPerDay) {
                list.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
            }
        }

        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getDateTime().toLocalTime().equals(startTime)
                    && !list.get(i).getDateTime().toLocalTime().isAfter(startTime)
                || !list.get(i).getDateTime().toLocalTime().equals(endTime)
                    && !list.get(i).getDateTime().toLocalTime().isBefore(endTime)) {
                list.remove(list.get(i));
                i--;
            }
        }
        return list;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededOptionalOne(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, IntSummaryStatistics> map = mealList.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), Collectors.summarizingInt(UserMeal::getCalories)));

        List<UserMealWithExceed> result = new ArrayList<>();
        mealList.stream()
                .filter(elem -> map.get(elem.getDateTime().toLocalDate()).getSum() > caloriesPerDay)
                .filter(elem ->
                            (elem.getDateTime().toLocalTime().equals(startTime)
                            || elem.getDateTime().toLocalTime().isAfter(startTime))
                        &&
                            (elem.getDateTime().toLocalTime().equals(endTime)
                            || elem.getDateTime().toLocalTime().isBefore(endTime)))
                .map(elem -> result.add(new UserMealWithExceed(elem.getDateTime(), elem.getDescription(), elem.getCalories(), true)))
                .collect(Collectors.toList());
        return result;
    }
}