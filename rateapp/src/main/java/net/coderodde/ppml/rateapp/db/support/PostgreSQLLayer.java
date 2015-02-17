package net.coderodde.ppml.rateapp.db.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import net.coderodde.ppml.rateapp.db.DBLayer;
import net.coderodde.ppml.rateapp.model.Movie;
import net.coderodde.ppml.rateapp.model.Rating;
import net.coderodde.ppml.rateapp.model.User;

/**
 *
 */
public class PostgreSQLLayer implements DBLayer {

    private static final class SQL {
        
        static final String ADD_USER_WITH_ID = 
                "INSERT INTO rateapp_users VALUES (?, ?, ?, ?, ?, ?);";
        
        static final String ADD_USER_WITHOUT_ID =
                "INSERT INTO rateapp_users VALUES " + 
                "((SELECT max(user_id) FROM rateapp_users) + 1, ?, ?, ?, ?, ?);";
        
        static final String ADD_MOVIE = 
                "INSERT INTO rateapp_movies VALUES (?, ?, ?, ?, ?, ?);";
        
        static final String ADD_RATING = 
                "INSERT INTO rateapp_ratings VALUES (?, ?, ?, ?);";
        
        static final String GET_USER_BY_NAME =
                "SELECT * FROM rateapp_users WHERE username = ?;";
        
        static final String GET_ALL_MOVIES = 
                "SELECT * FROM rateapp_movies;";
        
        static final String GET_USERS_RATINGS =
                "SELECT * FROM rateapp_ratings WHERE user_id = ?;";
    }
    
    private static final String DATABASE_LOOKUP_NAME = 
            "java:/comp/env/jdbc/rateappdb";
    
    public boolean addUser(final User user) {
        final Connection connection = openConnection();
        
        if (connection == null) {
            return false;
        }
        
        final PreparedStatement ps = getPreparedStatement(connection,
                                                          SQL.ADD_USER_WITH_ID);
        if (ps == null) {
            close(connection);
            return false;
        }
        
        try {
            ps.setInt(1, user.getUserID());
            ps.setString(2, user.getUserName());
            ps.setInt(3, user.getAge());
            ps.setString(4, user.getGender().toSQL());
            ps.setString(5, user.getOccupation());
            ps.setString(6, user.getZipCode());
            
            ps.executeUpdate();
            
            close(ps);
            close(connection);
            return true;
        } catch (final SQLException sqle) {
            close(ps);
            close(connection);
            return false;
        }   
    }

    @Override
    public boolean addUserByName(User user) {
        final Connection connection = openConnection();
        
        if (connection == null) {
            return false;
        }
        
        final PreparedStatement ps = 
                getPreparedStatement(connection,
                                     SQL.ADD_USER_WITHOUT_ID);
        
        if (ps == null) {
            close(connection);
            return false;
        }
        
        try {
            ps.setString(1, user.getUserName());
            ps.setInt(2, user.getAge());
            ps.setString(3, user.getGender().toSQL());
            ps.setString(4, user.getOccupation());
            ps.setString(5, user.getZipCode());
            
            ps.executeUpdate();
            
            close(ps);
            close(connection);
            return true;
        } catch (final SQLException sqle) {
            close(ps);
            close(connection);
            return false;
        }   
    }
    
    public boolean addMovie(Movie movie) {
        final Connection connection = openConnection();
        
        if (connection == null) {
            return false;
        }
        
        final PreparedStatement ps = getPreparedStatement(connection,
                                                          SQL.ADD_MOVIE);
        if (ps == null) {
            close(connection);
            return false;
        }
        
        try {
            ps.setInt(1, movie.getMovieID());
            ps.setString(2, movie.getMovieTitle());
            
            final Date release = movie.getReleaseDate();
            
            if (release != null) {
                ps.setDate(3, new java.sql.Date(release.getTime()));
            } else {
                ps.setDate(3, null);
            }
            
            final Date videoRelease = movie.getVideoReleaseDate();
            
            if (videoRelease != null) {
                ps.setDate(4, new java.sql.Date(videoRelease.getTime()));
            } else {
                ps.setDate(4, null);
            }
                
            ps.setString(5, movie.getIMDBUrl());
            
            final StringBuilder sb = new StringBuilder();
            
            for (final boolean b : movie.getGenreFlags()) {
                sb.append(b ? '1' : '0');
            }
            
            ps.setString(6, sb.toString());
            
            ps.executeUpdate();
            
            close(ps);
            close(connection);
            return true;
        } catch (final SQLException sqle) {
            close(ps);
            close(connection);
            return false;
        }
    }

    public boolean addRating(Rating rating) {
        final Connection connection = openConnection();
        
        if (connection == null) {
            return false;
        }
        
        final PreparedStatement ps = getPreparedStatement(connection,
                                                          SQL.ADD_RATING);
        if (ps == null) {
            close(connection);
            return false;
        }
        
        try {
            ps.setInt(1, rating.getUserID());
            ps.setInt(2, rating.getItemID());
            ps.setInt(3, rating.getScore());
            ps.setLong(4, rating.getTimestamp());
            
            ps.executeUpdate();
            
            close(ps);
            close(connection);
            return true;
        } catch (final SQLException sqle) {
            close(ps);
            close(connection);
            return false;
        }
    }

    @Override
    public List<Rating> getUsersRatings(User user) {
        final Connection connection = openConnection();
        
        if (connection == null) {
            return null;
        }
        
        final PreparedStatement ps = getPreparedStatement(connection,
                                                          SQL.GET_USERS_RATINGS);
        
        if (ps == null) {
            close(connection);
            return null;
        }
        
        try {
            ps.setInt(1, user.getUserID());
            final ResultSet rs = ps.executeQuery();
            final List<Rating> ratingList = extractRatings(rs);
            close(rs);
            close(ps);
            close(connection);
            return ratingList;
        } catch (final SQLException sqle) {
            close(ps);
            close(connection);
            sqle.printStackTrace(System.err);
            return null;
        }
    }
    
    @Override
    public User getUserByNickname(String nickname) {
        final Connection connection = openConnection();
        
        if (connection == null) {
            return null;
        }
        
        final PreparedStatement ps = getPreparedStatement(connection,
                                                          SQL.GET_USER_BY_NAME);
        
        if (ps == null) {
            close(connection);
            return null;
        }
        
        try {
            ps.setString(1, nickname);
            final ResultSet rs = ps.executeQuery();

            final User user = extractUser(rs);
            
            close(rs);
            close(ps);
            close(connection);
            
            return user;
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
            close(ps);
            close(connection);
            return null;
        }
    }
    
    public List<Movie> getAllMovies() {
        final Connection connection = openConnection();
        
        if (connection == null) {
            return null;
        }
        
        final Statement statement = getStatement(connection);
        
        if (statement == null) {
            close(connection);
            return null;
        }
        
        try {
            final ResultSet rs = statement.executeQuery(SQL.GET_ALL_MOVIES);
            final List<Movie> movieList = extractMovies(rs);
            close(rs);
            close(statement);
            close(connection);
            return movieList;
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
            close(statement);
            close(connection);
            return null;
        }
    }
    
    public List<User> getAllUsers() {
        return null;
    }
    
    private Connection openConnection() {
        try {
            final InitialContext ctx = new InitialContext();
            final DataSource ds = (DataSource) ctx.lookup(DATABASE_LOOKUP_NAME);
            return ds.getConnection();
        } catch (final NamingException ne) {
            ne.printStackTrace(System.err);
            return null;
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
    }
    
    private Statement getStatement(final Connection connection) {
        try {
            return connection.createStatement();
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
    }
    
    private PreparedStatement getPreparedStatement(final Connection connection,
                                                   final String template) {
        try {
            return connection.prepareStatement(template);
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
    }
    
    private void close(final Connection connection) {
        if (connection == null) {
            return;
        }
        
        try {
            connection.close();
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
    }
    
    private void close(final Statement statement) {
        if (statement == null) {
            return;
        }
        
        try {
            statement.close();
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
    }
    
    private void close(final ResultSet rs) {
        if (rs == null) {
            return;
        }
        
        try {
            rs.close();
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
    }
    
    private static List<Rating> extractRatings(final ResultSet rs) {
        try {
            final List<Rating> ratingList = new ArrayList<Rating>();
            
            while (rs.next()) {
                final int userId = rs.getInt(1);
                final int movieId = rs.getInt(2);
                final int score = rs.getInt(3);
                final long timestamp = rs.getLong(4);
                
                ratingList.add(new Rating(userId, movieId, score, timestamp));
            }
            
            return ratingList;
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
    }
    
    private static List<Movie> extractMovies(final ResultSet rs) {
        try {
            final List<Movie> movieList = new ArrayList<Movie>();
            
            while (rs.next()) {
                final int movieId = rs.getInt(1);
                final String movieTitle = rs.getString(2);
                final Date movieRelease = rs.getDate(3);
                final Date movieVideoRelease = rs.getDate(4);
                final String movieUrl = rs.getString(5);
                final String movieGenresRaw = rs.getString(6);
                
                final boolean[] genreFlags = 
                        new boolean[movieGenresRaw.length()];
                
                for (int i = 0; i < movieGenresRaw.length(); ++i) {
                    if (movieGenresRaw.charAt(i) != '0') {
                        genreFlags[i] = true;
                    }
                }
                
                movieList.add(new Movie(movieId,
                                        movieTitle,
                                        movieRelease,
                                        movieVideoRelease,
                                        movieUrl,
                                        genreFlags));
            }
            
            return movieList;
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
    }
    
    private static User extractUser(final ResultSet rs) {
        try {
            rs.next();
            
            final int userId = rs.getInt(1);
            final String username = rs.getString(2);
            final int age = rs.getInt(3);
            final String gender = rs.getString(4);
            final String occupation = rs.getString(5);
            final String zipCode = rs.getString(6);
            
            final User.Gender genderEnum = 
                    gender.equals("F") ? 
                    User.Gender.FEMALE :
                    (gender.equals("M") ?
                     User.Gender.MALE :
                     User.Gender.UNKNOWN);
            
            return new User(userId, 
                            username,
                            age,
                            genderEnum,
                            occupation,
                            zipCode);
            
        } catch (final SQLException sqle) {
            return null;
        }
    }
}
