package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.CrudDao;
import ru.javawebinar.topjava.dao.MealsDaoImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private CrudDao<Meal> mealsDao = new MealsDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get meals page");

        if (request.getParameter("id") != null && request.getParameter("action").equals("update")) {
            log.debug("get mealsEdit page");

            request.setAttribute("id", Integer.parseInt(request.getParameter("id")));
            request.getRequestDispatcher("/mealsEdit.jsp").forward(request, response);
        } else if (request.getParameter("id") != null  && request.getParameter("action").equals("delete")) {
            response.sendRedirect("meals");
        } else {
            List<Meal> meals = mealsDao.findAll();
            List<MealTo> allMeals = MealsUtil.get(meals, 2000);
            request.setAttribute("meals", allMeals);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("post meals page");

        req.setCharacterEncoding("UTF-8");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = null;
        Integer calories = null;
        Integer id = null;

        if (req.getParameter("action") != null && req.getParameter("action").equals("delete")) {
            log.debug("post delete meals page");

            id = Integer.parseInt(req.getParameter("id"));
            mealsDao.delete(id);
            resp.sendRedirect("meals");
        } else if (req.getParameter("action") != null && req.getParameter("action").equals("updateConfirm") ) {
            log.debug("post update meals page");
            try {
                id = Integer.parseInt(req.getParameter("id"));
                dateTime = LocalDateTime.parse(req.getParameter("calendar"), formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            try {
                calories = Integer.parseInt(req.getParameter("calories"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            String description = req.getParameter("description");
            mealsDao.update(new Meal(id, dateTime, description, calories));
            resp.sendRedirect("meals");
        } else {
            log.debug("post create page");
            try {
                dateTime = LocalDateTime.parse(req.getParameter("calendar"), formatter);
                calories = Integer.parseInt(req.getParameter("calories"));
            } catch (DateTimeParseException | NumberFormatException e) {
                e.printStackTrace();
            }
            String description = req.getParameter("description");
            mealsDao.create(new Meal(dateTime, description, calories));
            doGet(req, resp);
        }
    }
}