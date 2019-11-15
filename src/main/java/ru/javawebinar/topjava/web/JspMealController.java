package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

    @GetMapping("")
    public String getAll(Model model) {
        List<Meal> meals = service.getAll(SecurityUtil.authUserId());
        List<MealTo> mealsTo = MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay());
        model.addAttribute("meals", mealsTo);
        return "meals";
    }

    @GetMapping(params = "action")
    public String save(@NonNull @RequestParam("action") String action, Model model) {
        if ("create".equals(action)) {
            final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
            model.addAttribute("meal", meal);
            return "mealForm";
        }
        return "meals";
    }

    @GetMapping(params = {"action", "id"})
    public String getUpdateFormOrDelete(@NonNull @RequestParam("action") String action,
                                 @NonNull @RequestParam("id") String mealId,
                                 Model model) {
        int id = Integer.parseInt(mealId);
        switch (action) {
            case "update":
                model.addAttribute("meal", service.get(id, SecurityUtil.authUserId()));
                return "mealForm";
            case "delete":
                service.delete(id, SecurityUtil.authUserId());
        }
        return "redirect:meals";
    }

    @GetMapping(params = {"action", "startDate", "endDate", "startTime", "endTime"})
    public String filter(@NonNull @RequestParam("action") String action,
                         @Nullable @RequestParam("startDate") String startDateReq,
                         @Nullable @RequestParam("endDate") String endDateReq,
                         @Nullable @RequestParam("startTime") String startTimeReq,
                         @Nullable @RequestParam("endTime") String endTimeReq,
                         Model model) {
        if ("filter".equals(action)) {
            LocalDate startDate = parseLocalDate(startDateReq);
            LocalDate endDate = parseLocalDate(endDateReq);
            LocalTime startTime = parseLocalTime(startTimeReq);
            LocalTime endTime = parseLocalTime(endTimeReq);

            model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        }
        return "meals";
    }

    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Meal> mealsDateFiltered = service.getBetweenDates(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    @PostMapping("")
    public String setMeals(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        String id = request.getParameter("id");

        if (StringUtils.isEmpty(id)) {
            service.create(meal, SecurityUtil.authUserId());
        } else {
            assureIdConsistent(meal, Integer.parseInt(id));
            service.update(meal, SecurityUtil.authUserId());
        }
        return "redirect:meals";
    }
}
