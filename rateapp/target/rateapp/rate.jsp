<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>rateapp | rate your movies!</title>
    </head>
    <body>
        <h1>Rate your favorite movies!</h1>
        
        <form action="rate" method="post">
            <c:forEach var="movieAndRating" items="${requestScope.movieAndRatingList}">
                <c:out value="${movieAndRating.movie.movieTitle}<br>" />
            </c:forEach>
        </form>
    </body>
</html>
