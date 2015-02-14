package net.coderodde.ppml.rateapp.db;

import java.util.List;
import net.coderodde.ppml.rateapp.model.Movie;
import net.coderodde.ppml.rateapp.model.Rating;
import net.coderodde.ppml.rateapp.model.User;

/**
 * This interface defines the API for database layer.
 */
public interface DBLayer {
    
    public boolean addUser(final User user);
    
    public boolean addMovie(final Movie movie);
    
    public boolean addRating(final Rating rating);
    
    public List<User> getAllUsers();
    
    public User getUserByNickname(final String nickname);
}
