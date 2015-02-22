<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>rateapp | rate your movies!</title>
    </head>
    <body>
        <h1>Rate your favorite movies, ${username}!</h1>

        <form action="rate" method="post">
            <input type="submit" value="Rate!">
            <table>
                <c:forEach var="movieAndRating" items="${requestScope.movieAndRatingList}">
                    <tr>
                        <td><c:out value="${movieAndRating.movie.movieTitle}" /></td>
                        <td>
                            <select name="score_${movieAndRating.movie.movieID}">

                                <c:choose>
                                    <c:when test="${movieAndRating.rating != null && movieAndRating.rating.score == 5}">
                                        <option value="five" selected>5</option>
                                        <option value="four">4</option>
                                        <option value="three">3</option>
                                        <option value="two">2</option>
                                        <option value="one">1</option>
                                        <option value="none">0</option>
                                    </c:when>
                                    <c:when test="${movieAndRating.rating != null && movieAndRating.rating.score == 4}">
                                        <option value="five">5</option>
                                        <option value="four" selected>4</option>
                                        <option value="three">3</option>
                                        <option value="two">2</option>
                                        <option value="one">1</option>
                                        <option value="none">0</option>
                                    </c:when>
                                    <c:when test="${movieAndRating.rating != null && movieAndRating.rating.score == 3}">
                                        <option value="five">5</option>
                                        <option value="four">4</option>
                                        <option value="three" selected>3</option>
                                        <option value="two">2</option>
                                        <option value="one">1</option>
                                        <option value="none">0</option>
                                    </c:when>
                                    <c:when test="${movieAndRating.rating != null && movieAndRating.rating.score == 2}">
                                        <option value="five">5</option>
                                        <option value="four">4</option>
                                        <option value="three">3</option>
                                        <option value="two" selected>2</option>
                                        <option value="one">1</option>
                                        <option value="none">0</option>
                                    </c:when>
                                    <c:when test="${movieAndRating.rating != null && movieAndRating.rating.score == 1}">
                                        <option value="five">5</option>
                                        <option value="four">4</option>
                                        <option value="three">3</option>
                                        <option value="two">2</option>
                                        <option value="one" selected>1</option>
                                        <option value="none">0</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="five">5</option>
                                        <option value="four">4</option>
                                        <option value="three">3</option>
                                        <option value="two">2</option>
                                        <option value="one">1</option>
                                        <option value="none" selected>0</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <input type="hidden" name="username" value="${username}" />
            <input type="hidden" name="userid" value="${userid}" />
            <input type="submit" value="Rate!">
        </form>
    </body>
</html>
