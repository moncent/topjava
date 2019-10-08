<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>
    <table>
        <tr>
            <th>Дата/Время</th>
            <th>Описание</th>
            <th>Калории</th>
            <th colspan="2">Редактирование</th>
        </tr>
        <c:forEach items="${meals}" var="elem">
            <tr style="color: <c:out value="${elem.excess ? 'red' : 'green'}"/>">
                <javatime:parseLocalDateTime value="${elem.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="dt"/>
                <td><javatime:format value="${dt}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td>${elem.description}</td>
                <td>${elem.calories}</td>
                <td>
                    <a href="meals?id=${elem.id}&action=update">Change</a>
                </td>
                <td>
                    <form method="post" action="meals?id=${elem.id}&action=delete">
                        <input type="submit" value="Delete">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <br>
    <h3>Добавление еды</h3>
    <form method="post" action="meals">
        Дата/Время: <input type="datetime-local" name="calendar"><br>
        <br>
        Описание: <input type="text" name="description"><br>
        <br>
        Калории: <input type="number" name="calories"><br>
        <br>
        <input type="submit" name="button" value="Add">
    </form>
</body>
</html>