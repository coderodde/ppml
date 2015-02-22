package net.coderodde.ppml.rateapp.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.ppml.machinery.UserMatcher;
import net.coderodde.ppml.rateapp.db.DBLayer;
import net.coderodde.ppml.rateapp.db.support.PostgreSQLLayer;
import net.coderodde.ppml.rateapp.model.Movie;
import net.coderodde.ppml.rateapp.model.Rating;
import net.coderodde.ppml.rateapp.model.User;

/**
 * This servlet is responsible for receiving the users ratings and recommending
 * some movies based on the former.
 */
public class RecommenderServlet extends HttpServlet {

    public static final int NEIGHBOR_AMOUNT = 5;
    
    public static final int MAX_RECOMMENDATIONS = 5;
    
    private static final Map<String, Integer> mapTextToScore;
    
    static {
        mapTextToScore = new HashMap<String, Integer>();
        
        mapTextToScore.put("five",  5);
        mapTextToScore.put("four",  4);
        mapTextToScore.put("three", 3);
        mapTextToScore.put("two",   2);
        mapTextToScore.put("one",   1);
        mapTextToScore.put("none",  0);
    }
    
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
        final DBLayer dbl = new PostgreSQLLayer();
        
        final User user = 
                dbl.getUserByNickname(request.getParameter("username"));
        
        final Map<Rating, Rating> inputRatingMap = 
                getRatingMapFromRequest(request, user);
        
        for (final Rating rating : inputRatingMap.keySet()) {
            if (rating.getScore() == Rating.NOT_RATED) {
                if (dbl.hasRating(rating)) {
                    // The user wants to remove the current rating.
                    dbl.removeRating(rating);
                }
            } else {
                // The input rating has a non-zero score.
                if (dbl.hasRating(rating)) {
                    dbl.updateRating(rating);
                } else {
                    dbl.addRating(rating);
                }
            }
        }
        
        final List<User> userList = dbl.getAllUsers();
        final List<Movie> movieList = dbl.getAllMovies();
        final List<Rating> ratingList = dbl.getAllRatings();
        
        final UserMatcher um = new UserMatcher(userList,
                                               movieList,
                                               ratingList);
        
        final List<Movie> recommendedMovieList =
                um.gerRecommendations(user,
                                      NEIGHBOR_AMOUNT,
                                      MAX_RECOMMENDATIONS);
        
        request.setAttribute("recommended_movies", recommendedMovieList);
        request.setAttribute("username", user.getUserName());
        request.getRequestDispatcher("recommend.jsp")
               .forward(request, response);
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
        response.getOutputStream().println("GET-method not supported.");
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
        return "This servlet is responsible for receiving the ratings and " +
               "recommending other movies.";
    }
    
    /**
     * Returns a map mapping each rating to itself. This can be thought of
     * like an array indexed by the actual components.
     * 
     * @param  request the servlet request containing the attributes.
     * @param  user    the user whose ratings are being processed.
     * @return         a map.
     */
    private Map<Rating, Rating> 
        getRatingMapFromRequest(final HttpServletRequest request,
                                final User user) {
        final Enumeration<String> enumeration = request.getParameterNames();
        final Map<Rating, Rating> ratingMap = new HashMap<Rating, Rating>();
        final int userId = user.getUserID();
        
        while (enumeration.hasMoreElements()) {
            final String parameter = enumeration.nextElement();
            
            if (!parameter.contains("score")) {
                continue;
            }
            
            final int movieId = 
                    Integer.parseInt(parameter.split("_")[1]);
            final int score = mapTextToScore
                              .get(request.getParameter(parameter));
            final Rating rating = 
                    new Rating(userId,
                               movieId,
                               score,
                               System.currentTimeMillis() / 1000);
            
            ratingMap.put(rating, rating);
        }
        
        return ratingMap;
    }
}
