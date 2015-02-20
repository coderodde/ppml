<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>rateapp | recommendations</title>
    </head>
    <body>
        <h1>Recommended for you, ${username}!</h1>
        <ul>
            <c:forEach var="movie" items="${requestScope.recommended_movies}">
                <li><c:out value="${movie.movieTitle}" /></li>
            </c:forEach>
        </ul>
    </body>
</html>
