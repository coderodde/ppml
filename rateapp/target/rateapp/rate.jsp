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
                    <option value="five"  <c:if test="movieAndRating.rating != null && movieAndRating.rating.score == 5"> selected </c:if>>5</option>
                    <option value="four"  <c:if test="movieAndRating.rating != null && movieAndRating.rating.score == 4"> selected </c:if>>4</option>
                    <option value="three" <c:if test="movieAndRating.rating != null && movieAndRating.rating.score == 3"> selected </c:if>>3</option>
                    <option value="two"   <c:if test="movieAndRating.rating != null && movieAndRating.rating.score == 2"> selected </c:if>>2</option>
                    <option value="one"   <c:if test="movieAndRating.rating != null && movieAndRating.rating.score == 1"> selected </c:if>>1</option>
                    <option value="none"  <c:if test="movieAndRating.rating == null"> selected </c:if>>No rating</option>
                    </select></td>
                </tr>
            </c:forEach>
            </table>
            <input type="hidden" name="username" value="${username}" />
            <input type="hidden" name="userid" value="${userid}" />
            <input type="submit" value="Rate!">
        </form>
    </body>
</html>
