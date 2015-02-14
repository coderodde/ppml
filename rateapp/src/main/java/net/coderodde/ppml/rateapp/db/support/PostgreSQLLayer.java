package net.coderodde.ppml.rateapp.db.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
        
        static final String ADD_MOVIE = 
                "INSERT INTO rateapp_movies VALUES (?, ?, ?, ?, ?, ?);";
        
        static final String ADD_RATING = 
                "INSERT INTO rateapp_ratings VALUES (?, ?, ?, ?);";
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

    public List<Movie> getAllMovies() {
        return null;
    }
    
    public List<User> getAllUsers() {
        return null;
    }
    
    private Connection openConnection() {
//        final String url = "jdbc:postgresql://localhost/rodionef";
//        
//        try {
//            Class.forName("org.postgresql.Driver");
//        } catch (final ClassNotFoundException cnfe) {
//            return null;
//        }
//            
//        try {
//            return DriverManager.getConnection(url, 
//                                               "rodionef", 
//                                               "ab58a26cdfab5f1f");
//        } catch (final SQLException sqle) {
//            sqle.printStackTrace(System.err);
//            return null;
//        }
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
    
    private void close(final Connection connection) {
        try {
            connection.close();
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
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
    
    private void close(final Statement statement) {
        try {
            statement.close();
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
    }
}
