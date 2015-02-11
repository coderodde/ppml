package net.coderodde.ppml.rateapp.model;

/**
 * This class encompasses all the necessary information for a movie rating.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class Rating {

    /**
     * The user ID of a user.
     */
    private final int userId;
    
    /**
     * The item ID of the item being rated.
     */
    private final int movieId;
    
    /**
     * The score of this rating. It is up to the client programmer to decide
     * what is the rating scale.
     */
    private final int score;
    
    /**
     * The timestamp of this rating. It is up to the client programmer to decide
     * how this timestamp is interpreted.
     */
    private final long timestamp;
    
    /**
     * Constructs a new rating.
     * 
     * @param userId    the ID of the user of this rating.
     * @param itemId    the ID of the item being rated.
     * @param score     the score of the new rating.
     * @param timestamp the timestamp of the new rating.
     */
    public Rating(final int userId,
                  final int itemId,
                  final int score,
                  final long timestamp) {
        this.userId = userId;
        this.movieId = itemId;
        this.score = score;
        this.timestamp = timestamp;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Rating)) {
            return false;
        }
        
        final Rating other = (Rating) o;
        return getUserID() == other.getUserID() 
            && getItemID() == other.getItemID();
    }
    
    public int hashCode() {
        return Integer.hashCode(userId ^ movieId);
    }
    
    public int getUserID() {
        return userId;
    }
    
    public int getItemID() {
        return movieId;
    }
    
    public int getScore() {
        return score;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return new StringBuilder("[userId: ")
                   .append(userId)
                   .append(", movieId: ")
                   .append(movieId)
                   .append(", score: ")
                   .append(score)
                   .append(", timestamp: ")
                   .append(timestamp)
                   .append("]")
                   .toString();
    }
}
