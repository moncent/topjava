package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Meal m WHERE m.id = :id AND m.user.id = :userId")
    int deleteById(@Param("id") int id, @Param("userId") int userId);

    @Modifying
    @Query(value = "SELECT m FROM Meal m WHERE m.id = ?1 AND m.user.id = ?2")
    Meal getOne(int id, int userId);

    @Modifying
    @Query(value = "SELECT m FROM Meal m WHERE m.user.id = ?1")
    List<Meal> findAll(int userId, Sort sort);

    @Modifying
    @Query(value = "SELECT m FROM Meal m " +
            "WHERE m.user.id=?3 AND m.dateTime >= ?1 AND m.dateTime < ?2")
    List<Meal> findAll(LocalDate startDate, LocalDate endDate, int userId, Sort sort);
}
