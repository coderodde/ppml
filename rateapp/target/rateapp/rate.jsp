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
            <table>
            <c:forEach var="movieAndRating" items="${requestScope.movieAndRatingList}">
                <tr>
                <td><c:out value="${movieAndRating.movie.movieTitle}" /></td>
                <td><select name="score_${movieAndRating.movie.movieID}">
                    <option value="five">5</option>
                    <option value="four">4</option>
                    <option value="three">3</option>
                    <option value="two">2</option>
                    <option value="one">1</option>
                    <option value="none" selected>No rating</option>
                    </select></td>
                </tr>
            </c:forEach>
            </table>
            <input type="hidden" name="username" value="${userName}" />
            <input type="submit" value="Rate!">
        </form>
    </body>
</html>
