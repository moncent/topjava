package ru.javawebinar.topjava.to;

import java.time.LocalDateTime;
import java.util.Objects;

public class MealTo {
    private final Integer id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public Integer getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MealTo obj = (MealTo) o;
        return this.calories == obj.calories
                && this.excess == obj.excess
                && this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 37;
        hash = 39 * hash + (id == null ? 0 : id.hashCode());
        hash = 39 * hash + (dateTime == null ? 0 : dateTime.hashCode());
        hash = 39 * hash + (description == null ? 0 : description.hashCode());
        hash = 39 * hash + calories;
        hash = 39 * hash + (excess ? 1 : 0);
        return hash;
    }
}