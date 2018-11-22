package com.udacity.ahmed_eid.popularmoviesapp.utilities;

public class AppConstants {

    public final static String popularMovies = "Popular Movies (Default)";
    public final static String ratingMovies = "Highest Rate Movies";
    public final static String favoriteMovies = "Favorites Movies";

    public final static String popularMoviesUrl = "http://api.themoviedb.org/3/movie/popular?api_key=d356736ae8fd73791fa8988af49d36e8";
    public final static String ratingMoviesUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key=d356736ae8fd73791fa8988af49d36e8";

    public final static String jsonResultsTag = "Json Results Is: ";
    public final static String NetworkMassage = "Failed to get results, Not connected to the internet";
    public final static String errorMassage = "Failed to get results, Please try again.";
    public final static String NotFavoritesMassage = "Not Have Favorites Movies.";


    public static final String mIdKey = "movieId";
    public static final String mTitleKey = "movieTitle";
    public static final String mOverviewKey = "movieOverview";
    public static final String mReleaseDateKey = "movieReleaseDate";
    public static final String mPosterKey = "moviePoster";
    public static final String mRateKey = "movieRate";

    public static final String imageBaseUrl = "http://image.tmdb.org/t/p/";
    public static final String imageSize = "w185/";


}
