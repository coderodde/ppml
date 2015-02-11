package net.coderodde.ppml.model;

import java.util.Date;

public class Movie {

    private final int movieId;
    private final String movieTitle;
    private final Date releaseDate;
    private final Date videoReleaseDate;
    private final String imdbUrl;
    private final boolean[] genreFlags;
    
    public Movie(final int movieId,
                 final String movieTitle,
                 final Date releaseDate,
                 final Date videoReleaseDate,
                 final String imdbUrl,
                 final boolean[] genreFlags) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.releaseDate = releaseDate;
        this.videoReleaseDate = videoReleaseDate;
        this.imdbUrl = imdbUrl;
        this.genreFlags = genreFlags;
    }
    
    public int getMovieID() {
        return movieId;
    }
    
    public String getMovieTitle() {
        return movieTitle;
    }
    
    public Date getReleaseDate() {
        return releaseDate;
    }
    
    public Date getVideoReleaseDate() {
        return videoReleaseDate;
    }
    
    public String getIMDBUrl() {
        return imdbUrl;
    }
    
    public boolean hasGenre(final int genreIndex) {
        if (genreIndex >= genreFlags.length) {
            return false;
        }
        
        return genreFlags[genreIndex];
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        
        sb.append("[movieId: ")
          .append(movieId)
          .append(", movieTitle: ")
          .append(movieTitle)
          .append(", releaseDate: ");
        
        if (releaseDate != null) {
            sb.append(releaseDate.toString());
        } else {
            sb.append("null");
        }
        
        sb.append(", videoReleaseDate: ");
                
        if (videoReleaseDate != null) {
            sb.append(videoReleaseDate.toString());
        } else {
            sb.append("null");
        }
                
        sb.append(", imdbUrl: ")
          .append(imdbUrl)
          .append(", genreFlags: ");
          
        for (int i = 0; i < genreFlags.length; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            
            sb.append(genreFlags[i] ? "1" : "0");
        }
        
        return sb.append("]").toString();
    }
}
