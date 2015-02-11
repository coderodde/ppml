package net.coderodde.ppml.rateapp.db.support;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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

    private static final String DATABASE_LOOKUP_NAME = "";
    
    public boolean addUser(User user) {
        return false;
    }

    public boolean addMovie(Movie movie) {
        return false;
    }

    public boolean addRating(Rating rating) {
        return false;
    }

    public List<Movie> getAllMovies() {
        return null;
    }
    
    private boolean userTableExists() {
        return false;
    }
    
    private boolean movieTableExists() {
        return false;
    }
    
    private boolean ratingTableExists() {
        return false;
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
    
    private void closeConnection(final Connection connection) {
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
}
