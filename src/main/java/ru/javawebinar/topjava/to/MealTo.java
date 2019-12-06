package ru.javawebinar.topjava.to;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class MealTo extends BaseTo implements Serializable {
    private static final long serialVersionUID = 9006955765770696329L;

    @NotNull(message = "wrong date/time!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

    @NotBlank
    @Size(min = 2, max = 120, message = "the length of description must be from 2 to 120")
    private String description;

    @NotNull(message = "wrong number of calories!")
    @Range(min = 10, max = 5000, message = "the number of calories must be from 10 to 5000")
    private Integer calories;

    private boolean excess;

    @ConstructorProperties({"id", "dateTime", "description", "calories", "excess"})
    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public MealTo(){

    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExcess() {
        return excess;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setExcess(boolean excess) {
        this.excess = excess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealTo mealTo = (MealTo) o;
        return excess == mealTo.excess &&
                Objects.equals(dateTime, mealTo.dateTime) &&
                Objects.equals(description, mealTo.description) &&
                Objects.equals(calories, mealTo.calories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, description, calories, excess);
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}