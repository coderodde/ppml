package net.coderodde.ppml.rateapp.model;

public class MovieAndRating {

    private Movie movie;
    private Rating rating;
    
    public MovieAndRating(final Movie movie, final Rating rating) {
        this.movie = movie;
        this.rating = rating;
    }
    
    public Movie getMovie() {
        return movie;
    }
    
    public Rating getRating() {
        return rating;
    }
}
