package net.coderodde.ppml.rateapp.db;

import java.util.List;
import net.coderodde.ppml.rateapp.model.Movie;
import net.coderodde.ppml.rateapp.model.Rating;
import net.coderodde.ppml.rateapp.model.User;

/**
 * This interface defines the API for database layer.
 */
public interface DBLayer {
    
    /**
     * Explicitly stores the user with all user fields specified.
     * 
     * @param  user the object representing a user.
     * 
     * @return <code>true</code> if storing to the database was successful,
     *         <code>false</code> otherwise.
     */
    public boolean addUser(final User user);
    
    /**
     * Does the same as <code>addUser(User)</code>, but let's the database
     * decide the ID of the new user.
     * 
     * @param  user the object specifying the user details. The ID field is 
     *         ignored.
     * @return <code>true</code> if storing to the database was successful,
     *         <code>false</code> otherwise.
     */
    public boolean addUserByName(final User user);
    
    /**
     * Adds a movie to the database.
     * 
     * @param  movie the movie to add.
     * @return <code>true</code> upon success, <code>false</code> otherwise.
     */
    public boolean addMovie(final Movie movie);
    
    public boolean addRating(final Rating rating);
    
    public List<User> getAllUsers();
    
    public User getUserByNickname(final String nickname);
    
    public List<Movie> getAllMovies();
    
    public List<Rating> getUsersRatings(final User user);
    
    public Boolean hasRating(final Rating rating);
    
    public boolean updateRating(final Rating rating);
    
    public boolean removeRating(final Rating rating);
}
