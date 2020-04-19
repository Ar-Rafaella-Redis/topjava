<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %><%--
  Created by IntelliJ IDEA.
  User: Anna
  Date: 18.04.2020
  Time: 23:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
    <style>
     .normal { color: green;}
     .excess { color: red;}
   </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border="1">
    <tr>
        <th>Time</th>
        <th>Description</th>
        <th>Calories</th>
      </tr>
    <c:forEach var="meal" items="${mealsList}" >
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr class="${meal.excess ? 'excess' : 'normal'}">
            <td><%=TimeUtil.toString(meal.getDateTime())%></td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
