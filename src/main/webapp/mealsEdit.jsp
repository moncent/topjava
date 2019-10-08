<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Meal</title>
</head>
    <body>
        <h3>Изменить пищу</h3>
        <form method="post" action="meals?id=${id}&action=updateConfirm">
            Дата/Время: <input type="datetime-local" name="calendar"><br>
            <br>
            Описание: <input type="text" name="description"><br>
            <br>
            Калории: <input type="number" name="calories"><br>
            <br>
            <input type="submit" name="button" value="Change">
        </form>
    </body>
</html>
