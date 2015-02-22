package net.coderodde.ppml.machinery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.coderodde.ppml.ds.SimpleHeap;
import net.coderodde.ppml.rateapp.model.Movie;
import net.coderodde.ppml.rateapp.model.Rating;
import net.coderodde.ppml.rateapp.model.User;

/**
 * This class is responsible for finding <tt>k</tt> nearest users to a given
 * user.
 * 
 */
public class UserMatcher {

    private final Set<Movie> auxMovieSet;
    private final List<User> userList;
    private final Map<User, Set<Movie>> mapUserToMovieSet;
    private final Map<Rating, Movie> mapRatingToMovie;
    private final Map<Integer, Movie> mapMovieIDtoMovie;
    private final Map<Integer, User> mapUserIDtoUser;
    private final Map<User, Map<Movie, Rating>> mapUserMovieToRating;
    
    public UserMatcher(final List<User> userList, 
                       final List<Movie> movieList,
                       final List<Rating> ratingList) {
        this.auxMovieSet = new HashSet<Movie>(); 
        this.userList = userList;
        this.mapUserToMovieSet = new HashMap<User, Set<Movie>>(userList.size());
        this.mapRatingToMovie = new HashMap<Rating, Movie>(ratingList.size());
        this.mapMovieIDtoMovie = new HashMap<Integer, Movie>(movieList.size());
        this.mapUserIDtoUser = new HashMap<Integer, User>(userList.size());
        this.mapUserMovieToRating = 
                new HashMap<User, Map<Movie, Rating>>(userList.size());
        
        for (final Movie movie : movieList) {
            mapMovieIDtoMovie.put(movie.getMovieID(), movie);
        }
        
        for (final Rating rating : ratingList) {
            mapRatingToMovie.put(rating, 
                                 mapMovieIDtoMovie.get(rating.getItemID()));
        }
        
        for (final User user : userList) {
            mapUserToMovieSet.put(user, new HashSet<Movie>());
            mapUserIDtoUser.put(user.getUserID(), user);
            mapUserMovieToRating.put(
                    user,
                    new HashMap<Movie, Rating>(movieList.size()));
        }
        
        for (final Rating rating : ratingList) {
            final int userId = rating.getUserID();
            final int movieId = rating.getItemID();
            
            final User user = mapUserIDtoUser.get(userId);
            final Movie movie = mapMovieIDtoMovie.get(movieId);
            
            mapUserToMovieSet.get(user).add(movie);
            mapUserMovieToRating.get(user).put(movie, rating);
        }
    }
    
    public Set<Movie> getUsersMovies(final User user) {
        return Collections.<Movie>unmodifiableSet(mapUserToMovieSet.get(user));
    }
    
    public List<User> match(final User targetUser, final int amount) {
        final SimpleHeap<User, Float> heap = new SimpleHeap<User, Float>();
        
        for (final User trialUser : userList) {
            if (trialUser.equals(targetUser)) {
                continue;
            }
            
            final float coefficient = 
                    jaccardCoefficient(trialUser,
                                       targetUser);
            
            heap.insert(trialUser, coefficient);
            
            if (heap.size() > amount) {
                // Keep only 'amount' hottest users!
                
                heap.extractMinimum();
            }
        }
        
        return heap.snapshot();
    }
    
    /**
     * Returns at most <code>maxRecommendations</code> recommended movies for 
     * the user <code>target</code>. Basically this routine performs
     * <tt>k</tt>-nearest neighbor algorithm, where <tt>k</tt> is specified by
     * <code>neighborAmount</code>.
     * 
     * @param target             the user for which we are computing
     *                           recommendations.
     * @param neighborAmount     the amount of neighbors to find.
     * @param maxRecommendations the maximum amount of recommendations.
     * 
     * @return a list of recommended movies.
     */
    public List<Movie> getRecommendations(final User target,
                                          final int neighborAmount,
                                          final int maxRecommendations) {
        final List<User> neighborList = match(target, neighborAmount);
        
        final Map<Movie, Integer> mapMovieToRatingScore = 
                new HashMap<Movie, Integer>();
        
        final Map<Movie, Integer> mapMovieToRatingAmount = 
                new HashMap<Movie, Integer>();
        
        for (final User neighbor : neighborList) {
            final Set<Movie> neighborsMovieSet = 
                    mapUserToMovieSet.get(neighbor);
            
            for (final Movie movie : neighborsMovieSet) {
                if (!mapMovieToRatingAmount.containsKey(movie)) {
                    mapMovieToRatingAmount.put(movie, 1);
                    mapMovieToRatingScore.put(movie,
                                              mapUserMovieToRating
                                              .get(neighbor)
                                              .get(movie).getScore());
                } else {
                    mapMovieToRatingAmount
                            .put(movie,
                                 mapMovieToRatingAmount.get(movie) + 1);
                    
                    mapMovieToRatingScore
                            .put(movie,
                                 mapMovieToRatingScore.get(movie) + 
                                 mapUserMovieToRating
                                 .get(neighbor)
                                 .get(movie).getScore());
                }
            }
        }
        
        final Map<Movie, Float> averageMovieRating = 
                new HashMap<Movie, Float>();
        
        for (final Movie movie : mapMovieToRatingAmount.keySet()) {
            final float averageScore = 1.0f * mapMovieToRatingScore.get(movie) /
                                              mapMovieToRatingAmount.get(movie);
            averageMovieRating.put(movie, averageScore);
        }
        
        final Set<Movie> set = new HashSet<Movie>();
        final List<Movie> ret = new ArrayList<Movie>(maxRecommendations);
        
        for (final User neighbor : neighborList) {
            set.addAll(mapUserToMovieSet.get(neighbor));
        }
        
        set.removeAll(mapUserToMovieSet.get(target));
        
        if (set.isEmpty()) {
            return ret;
        }
        
        final List<Movie> work = new ArrayList<Movie>(set.size());
        
        work.addAll(set);
        
        Collections.sort(work, new MovieComparator(averageMovieRating));
        
        for (int i = 0; i < maxRecommendations && work.size() > 0; ++i) {
            ret.add(work.remove(work.size() - 1));
        }
        
        return ret;
    }
    
    public float jaccardCoefficient(final User usera, final User userb) {
        auxMovieSet.clear();
        
        final Set<Movie> seta = mapUserToMovieSet.get(usera);
        final Set<Movie> setb = mapUserToMovieSet.get(userb);
        
        if (seta.size() < setb.size()) {
            for (final Movie movie : seta) {
                if (setb.contains(movie)) {
                    auxMovieSet.add(movie);
                }
            }
        } else {
            for (final Movie movie : setb) {
                if (seta.contains(movie)) {
                    auxMovieSet.add(movie);
                }
            }
        }
        
        float ratingFactor = 1.0f * auxMovieSet.size();
        
        final Map<Movie, Rating> mapa = mapUserMovieToRating.get(usera);
        final Map<Movie, Rating> mapb = mapUserMovieToRating.get(userb);
        
        for (final Movie movie : auxMovieSet) {
            final Rating ra = mapa.get(movie);
            final Rating rb = mapb.get(movie);
            
            ratingFactor -= Math.abs(ra.getScore() - rb.getScore()) / 5.0f;
        }
        
        return ratingFactor / 
              (seta.size() + setb.size() - auxMovieSet.size());
    }
    
    /**
     * This comparator ensures that upon sorting movies, the movies are ordered
     * into ascending order by their average score, so the "hottest" films end 
     * to the right side of the array.
     */
    private class MovieComparator implements Comparator<Movie> {
        
        private final Map<Movie, Float> averageScoreMap;
        
        MovieComparator(final Map<Movie, Float> averageScoreMap) {
            this.averageScoreMap = averageScoreMap;
        }
        
        @Override
        public int compare(final Movie movie1, final Movie movie2) {
            return Float.compare(averageScoreMap.get(movie1),
                                 averageScoreMap.get(movie2));
        }
    }
}
