package ru.javawebinar.topjava.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.StringJoiner;

@Controller
public class RootController {

    public static ResponseEntity<String> handleErrors (BindingResult result) {
        StringJoiner joiner = new StringJoiner("<br>");
        result.getFieldErrors().forEach(fe -> {
            String msg = fe.getDefaultMessage();
            if (msg != null) {
                if (!msg.startsWith(fe.getField())) {
                    msg = fe.getField() + ' ' + msg;
                }
                joiner.add(msg);
            }
        });
        return ResponseEntity.unprocessableEntity().body(joiner.toString());
    }

    @GetMapping("/")
    public String root() {
        return "redirect:meals";
    }

    @GetMapping("/users")
    public String getUsers() {
        return "users";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping("/meals")
    public String getMeals() {
        return "meals";
    }
}
