package net.coderodde.ppml;

import com.google.common.collect.BiMap;
import java.io.File;
import java.util.List;
import java.util.Random;
import net.coderodde.ppml.loader.DataLoader;
import net.coderodde.ppml.machinery.UserMatcher;
import net.coderodde.ppml.model.Movie;
import net.coderodde.ppml.model.Rating;
import net.coderodde.ppml.model.User;

public class App {

    private static final String DEFAULT_DATA_PATH = "../data/ml-100k";
    private static final String USER_FILE_NAME = "u.user";
    private static final String MOVIE_FILE_NAME = "u.item";
    private static final String RATINGS_FILE_NAME = "u.data";
    private static final String GENRE_FILE_NAME = "u.genre";
    
    public static void main(final String... args) {
        final long seed = System.currentTimeMillis();
        final Random rnd = new Random(seed);
        
        System.out.println("Seed: " + seed);
        
        final String path = args.length == 0 ? 
                                 DEFAULT_DATA_PATH : 
                                 args[0];
        
        final List<User> userList = 
                DataLoader.loadUsers(path + File.separator + USER_FILE_NAME);
        
//        for (final User user : userList) {
//            System.out.println(user.toString());
//        }
        
        System.out.println("Total users: " + userList.size());
        
        final BiMap<String, Integer> map = 
                DataLoader.loadGenreMap(
                        path + File.separator + GENRE_FILE_NAME);
        
        System.out.println(map);
        
        final List<Movie> movieList = 
                DataLoader.loadMovies(path + File.separator + MOVIE_FILE_NAME);
        
//        for (final Movie movie : movieList) {
//            System.out.println(movie.toString());
//        }
        
        System.out.println("Total movies: " + movieList.size());
        
        final List<Rating> ratingList =
                DataLoader.loadRatings(
                        path + File.separator + RATINGS_FILE_NAME);
         
        System.out.println("Total ratings: " + ratingList.size());
        System.out.println("Target user's movies:");
        
        final User target = userList.get(rnd.nextInt(userList.size()));
        final UserMatcher matcher = 
                new UserMatcher(userList, movieList, ratingList);
        
        for (final Movie movie : matcher.getUsersMovies(target)) {
            System.out.println(movie);
        }
        
        long ta = System.currentTimeMillis();
        final List<User> neighborList = matcher.match(target, 3);
        long tb = System.currentTimeMillis();
        
        System.out.println("Matching time: " + (tb - ta) + " ms.");
        System.out.println();
        System.out.println("Neighbors:");
        
        for (final User neighbor : neighborList) {
            System.out.println("Jaccard coefficient: " + 
                    matcher.jaccardCoefficient(
                            target,
                            neighbor));
            
            for (final Movie movie : matcher.getUsersMovies(neighbor)) {
                System.out.println(movie);
            }
            
            System.out.println();
        }
        
        final List<Movie> recommendations = matcher.gerRecommendations(target, 3, 5);
        
        System.out.println("Recommendations:");
        
        for (final Movie movie : recommendations) {
            System.out.println(movie);
        }
    }
}
