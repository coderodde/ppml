package net.coderodde.ppml.loader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import net.coderodde.ppml.rateapp.model.Movie;
import net.coderodde.ppml.rateapp.model.Rating;
import net.coderodde.ppml.rateapp.model.User;
import net.coderodde.ppml.rateapp.model.User.Gender;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DataLoader {

    private static final int USER_ATTRIBUTES = 5;
    private static final int MOVIE_BASIC_ATTRIBUTES = 5;
    private static final int GENRE_ATTRIBUTES = 2;
    private static final int RATING_ATTRIBUTES = 4;
    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    
    public static List<User> loadUsers(final String path) {
        final File file = new File(path);
        
        if (!checkFile(file)) {
            return null;
        }
        
        Scanner scanner = null;
        final List<User> userList = new ArrayList<User>();
        
        try {
            scanner = new Scanner(new FileReader(file));
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
        
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final User user = extractUser(line);
            
            if (user != null) {
                userList.add(user);
            }
        }
        
        
        return userList;
    }
    
    public static List<Movie> loadMovies(final String path) {
        final File file = new File(path);
        
        if (!checkFile(file)) {
            return null;
        }
        
        Scanner scanner = null;
        final List<Movie> movieList = new ArrayList<Movie>();
        
        try {
            scanner = new Scanner(new FileReader(file));
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
        
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final Movie movie = extractMovie(line);
            
            if (movie != null) {
                movieList.add(movie);
            }
        }
        
        return movieList;
    }
    
    public static List<Rating> loadRatings(final String path) {
        final File file = new File(path);
        
        if (!checkFile(file)) {
            return null;
        }
        
        Scanner scanner = null;
        final List<Rating> ratingList = new ArrayList<Rating>();
        
        try {
            scanner = new Scanner(new FileReader(file));
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
        
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final Rating rating = extractRating(line);
            
            if (rating != null) {
                ratingList.add(rating);
            } 
        }
        
        return ratingList;
    }
    
    public static BiMap<String, Integer> loadGenreMap(final String path) {
        final File file = new File(path);
        
        if (!checkFile(file)) {
            return null;
        }
        
        Scanner scanner = null;
        final BiMap<String, Integer> map = HashBiMap.create();
        
        try {
            scanner = new Scanner(file);
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
        
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine().trim();
            final String[] parts = line.split("\\|");
            
            if (parts.length == GENRE_ATTRIBUTES) {
                map.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
            }
        }
        
        
        return map;
    }
    
    private static User extractUser(final String line) {
        final String[] parts = line.split("\\|");
        
        if (parts.length != USER_ATTRIBUTES) {
            return null;
        }
        
        final int userId = Integer.parseInt(parts[0].trim());
        final int age =    Integer.parseInt(parts[1].trim());
        
        final String g = parts[2].trim().toUpperCase();
        
        final Gender gender = (g.equals("M") ? 
                               Gender.MALE : (g.equals("F") ?
                                              Gender.FEMALE : Gender.UNKNOWN));
        
        return new User(userId, 
                        null,
                        age, 
                        gender, 
                        parts[3].trim().toLowerCase(), 
                        parts[4].trim().toLowerCase());
    }
    
    private static Movie extractMovie(final String line) {
        final String[] parts = line.trim().split("\\|");
        
        if (parts.length < MOVIE_BASIC_ATTRIBUTES) {
            return null;
        }
        
        final int movieId = Integer.parseInt(parts[0].trim());
        final String movieName = parts[1].trim();
        final String releaseDateString = parts[2].trim();
        final String videoReleaseDateString = parts[3].trim();
        final String imdbUrl = parts[4].trim();
        
        final boolean[] genreFlags = 
                new boolean[parts.length - MOVIE_BASIC_ATTRIBUTES];
        
        for (int i = MOVIE_BASIC_ATTRIBUTES; i < parts.length; ++i) {
            if (!parts[i].equals("0")) {
                genreFlags[i - MOVIE_BASIC_ATTRIBUTES] = true;
            }
        }
        
        final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        
        Date releaseDate = null;
        Date videoReleaseDate = null;
        
        if (!releaseDateString.isEmpty()) {
            try {
                releaseDate = formatter.parse(releaseDateString);
            } catch (final ParseException pe) {
                
            }
        }
        
        if (!videoReleaseDateString.isEmpty()) {
            try {
                videoReleaseDate = formatter.parse(videoReleaseDateString);
            } catch (final ParseException pe) {
                
            }
        }
        
        return new Movie(movieId,
                         movieName,
                         releaseDate,
                         videoReleaseDate,
                         imdbUrl,
                         genreFlags);
    }
    
    private static Rating extractRating(final String line) {
        final String[] parts = line.split("\\s+");
        
        if (parts.length != RATING_ATTRIBUTES) {
            System.out.println(parts.length);
            return null;
        }
        
        final int userId     = Integer.parseInt(parts[0].trim());
        final int movieId    = Integer.parseInt(parts[1].trim());
        final int score      = Integer.parseInt(parts[2].trim());
        final long timestamp = Long.parseLong(parts[3].trim());
        
        return new Rating(userId, movieId, score, timestamp);
    }
    
    /**
     * Returns <code>true</code> if the file seems to exist and is an actual
     * file and otherwise returns <code>false</code>.
     * 
     * @param  file the file to check.
     * @return <code>true</code> if the file seems to exist and is an actual 
     *         file, otherwise <code>false</code> is returned.
     */
    private static boolean checkFile(final File file) {
        return file.exists() && file.isFile();
    }
}
