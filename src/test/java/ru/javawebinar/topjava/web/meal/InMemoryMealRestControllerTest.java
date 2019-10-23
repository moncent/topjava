package ru.javawebinar.topjava.web.meal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

@ContextConfiguration("classpath:spring/spring-app-test.xml")
@RunWith(SpringRunner.class)
public class InMemoryMealRestControllerTest {

    @Autowired
    private MealRestController controller;

    @Autowired
    private InMemoryMealRepository repository;

    @Before
    public void setUp() throws Exception {
        repository.init();
    }


    @Test
    public void get() {
        controller.get(MealTestData.MEAL_ID1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        controller.get(MealTestData.MEAL_ID3);
    }

    @Test
    public void deleteUserMeal() throws Exception {
        controller.delete(MealTestData.MEAL_ID1);

    }

    @Test(expected = NotFoundException.class)
    public void deleteNotUserMeal() {
        controller.delete(MealTestData.MEAL_ID3);
    }


    @Test
    public void getAll() {
        controller.getAll();
    }


    @Test
    public void create() {
    }

    @Test
    public void update() {
    }

    @Test
    public void getBetween() {
    }
}