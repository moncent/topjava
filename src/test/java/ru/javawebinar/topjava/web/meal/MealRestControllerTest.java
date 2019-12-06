package ru.javawebinar.topjava.web.meal;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.*;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.util.MealsUtil.createTo;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(readFromJsonMvcResult(result, Meal.class), MEAL1));
    }

    @Test
    void getUnAuthAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    void update() throws Exception {
        MealTo updatedTo = new MealTo(null, LocalDateTime.of(2019,12,6,12,0), "newDescription", 1000, false);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MEAL2.getId()).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(mealService.get(MEAL2.getId(), START_SEQ), MealsUtil.updateFromTo(MEAL2, updatedTo));
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = getNew();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)));

        Meal created = readFromJson(action, Meal.class);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(getTos(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    void filter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "2015-05-30").param("startTime", "07:00")
                .param("endDate", "2015-05-31").param("endTime", "11:00")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(contentJson(createTo(MEAL5, true), createTo(MEAL1, false)));
    }

    @Test
    void filterAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter?startDate=&endTime=")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(contentJson(getTos(MEALS, USER.getCaloriesPerDay())));
    }
}