<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>rateapp | home</title>
    </head>
    <body>
        <h1>Welcome to rateapp!</h1>
        <form action="login" method="post">
            Nickname:<br>
            <input type="text" name="nickname" value=""><br>
            Age:<br>
            <input type="number" name="age"><br>
            Gender:<br>
            <input type="radio" name="gender" value="Female">
            <input type="radio" name="gender" value="Male">
            <input type="radio" name="gender" value="Unknown" checked>
            Occupation:<br>
            <input type="text" name="occupation"><br>
            ZIP code:<br>
            <input type="text" name="zipcode"><br>
            <input type="submit" value="Go!">
        </form>
    </body>
</html>
