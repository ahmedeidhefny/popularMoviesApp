package com.udacity.ahmed_eid.popularmoviesapp.utilities;

import com.udacity.ahmed_eid.popularmoviesapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JsonUtils {

    public static ArrayList<Movie> parseMoviesJson(String json) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray Movies = jsonObject.getJSONArray("results");

        for (int i = 0; i < Movies.length(); i++) {
            JSONObject movie = Movies.getJSONObject(i);
            int mId = Integer.parseInt(movie.optString("id"));
            String mTitle = movie.optString("title");
            String mOverview = movie.optString("overview");
            float mRate = Float.parseFloat(movie.optString("vote_average"));
            String mPoster = movie.optString("poster_path");

            String mReleaseDateString = movie.optString("release_date");
            Date mReleaseDate = null;
            if (mReleaseDateString != null) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    mReleaseDate = dateFormat.parse(mReleaseDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            movies.add(i, new Movie(mId, mTitle, mOverview, mReleaseDate, mPoster, mRate));
        }

        return movies;
    }
}
