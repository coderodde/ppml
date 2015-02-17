package net.coderodde.ppml.rateapp.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.ppml.rateapp.db.DBLayer;
import net.coderodde.ppml.rateapp.db.support.PostgreSQLLayer;
import net.coderodde.ppml.rateapp.model.Movie;
import net.coderodde.ppml.rateapp.model.MovieAndRating;
import net.coderodde.ppml.rateapp.model.Rating;
import net.coderodde.ppml.rateapp.model.User;

/**
 * This servlet is responsible for logging a user in. After the request, this
 * servlet shows the list of all movies for the user to rate. However, if the 
 * user already exists, the presented movie list also shows all the previous
 * ratings the user has created.
 */
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        final String nickname = request.getParameter("nickname");
        
        if (nickname == null || nickname.isEmpty()) {
            request.getRequestDispatcher("/")
                   .forward(request, response);
            return;
        }
        
        final DBLayer dbl = new PostgreSQLLayer();
        
        User user = dbl.getUserByNickname(nickname);
        
        final ServletOutputStream os = response.getOutputStream();
        
        if (user == null) {
            // User with nickname is not in the DB. Try adding.
            final User u = new User(0,
                                    nickname,
                                    -1,
                                    User.Gender.UNKNOWN,
                                    "N/A",
                                    "N/A");
            
            final boolean added = dbl.addUserByName(u);
            
            os.println("Added to DB: " + added + ".<br>");
            
            user = dbl.getUserByNickname(nickname);
            
            if (user != null) {
                os.println("User created!<br>");
            } else {
                os.println("DB refused to create a user!<br>");
                return;
            }
        } else {
            os.println("User already exists!<br>");
        }
        
        // Show the list of all movies with the facilities for rating.
        
        final List<Movie> movieList = dbl.getAllMovies();
        
        os.println("Got " + movieList.size() + " movies from DB!<br>");
        
        final List<Rating> ratingList = dbl.getUsersRatings(user);
        
        os.println("Got " + ratingList.size() + " ratings for the user!<br>");
        
        final List<MovieAndRating> marList = 
                new ArrayList<MovieAndRating>(movieList.size());
        
        
        final Map<Movie, Rating> map = 
                new HashMap<Movie, Rating>(movieList.size());
        
        final Map<Integer, Movie> movieMap =
                new HashMap<Integer, Movie>(movieList.size());
        
        for (final Movie movie : movieList) {
            movieMap.put(movie.getMovieID(), movie);
        }
        
        for (final Rating rating : ratingList) {
            map.put(movieMap.get(rating.getItemID()), rating);
        }
        
        for (final Movie movie : movieList) {
            final MovieAndRating mar = new MovieAndRating(movie, map.get(movie));
            marList.add(mar);
        }
        
        request.setAttribute("movieAndRatingList", marList);
        request.getRequestDispatcher("rate.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This servlet is responsible for logging a user in and " +
               "presenting the list of all movies.";
    }
}
