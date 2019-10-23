package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;


public class MealTestData {
    public static final int MEAL_ID1 = START_SEQ + 2;
    public static final int MEAL_ID2 = START_SEQ + 3;
    public static final int MEAL_ID3 = START_SEQ + 4;
    public static final int MEAL_ID4 = START_SEQ + 5;
    public static final int MEAL_ID5 = START_SEQ + 6;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static final Meal meal1 = new Meal(MEAL_ID1, LocalDateTime.parse("2019-10-18 10:00", formatter), "Завтрак", 800);
    public static final Meal meal2 = new Meal(MEAL_ID2, LocalDateTime.parse("2019-10-18 17:00", formatter), "Ужин", 700);
    public static final Meal meal3 = new Meal(MEAL_ID3, LocalDateTime.parse("2019-10-18 11:00", formatter), "Завтрак", 900);
    public static final Meal meal4 = new Meal(MEAL_ID4, LocalDateTime.parse("2019-10-18 13:00", formatter), "Обед", 900);
    public static final Meal meal5 = new Meal(MEAL_ID5, LocalDateTime.parse("2019-10-18 17:30", formatter), "Ужин", 800);



//          ('2019-10-18 10:00', 'Завтрак', 800, 100000),
//                  ('2019-10-18 17:00', 'Ужин', 700, 100000),
//                  ('2019-10-18 11:00', 'Завтрак', 900, 100001),
//                  ('2019-10-18 13:00', 'Обед', 900, 100000),
//                  ('2019-10-18 17:30', 'Ужин', 1000, 100001);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).matches(meal -> meal.getId().equals(expected.getId())
                                            && meal.getDescription().equals(expected.getDescription())
                                            && meal.getDateTime().equals(expected.getDateTime())
                                            && meal.getCalories() == expected.getCalories());
    }




}
