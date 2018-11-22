package com.udacity.ahmed_eid.popularmoviesapp.Activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.udacity.ahmed_eid.popularmoviesapp.DataBase.MainViewModel;
import com.udacity.ahmed_eid.popularmoviesapp.Adapters.MyGridAdapter;
import com.udacity.ahmed_eid.popularmoviesapp.Adapters.MyRecyclerReviewsAdapter;
import com.udacity.ahmed_eid.popularmoviesapp.Adapters.MyRecyclerTrailersAdapter;
import com.udacity.ahmed_eid.popularmoviesapp.R;
import com.udacity.ahmed_eid.popularmoviesapp.model.Movie;
import com.udacity.ahmed_eid.popularmoviesapp.utilities.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MovieDetails extends AppCompatActivity implements View.OnClickListener {
    private TextView mTitleTV, mOverviewTV, mReleaseDateTV, mRatingTV;
    private ImageView mPosterIV;

    private ImageView redFavorite, whiteFavorite;

    private TextView errorReview, errorTrailer;

    private MainViewModel viewModel;
    private Movie mMovie;

    private ArrayList<String> trailersNames, trailersImages, keys;
    private ArrayList<String> authors, contents;
    private RecyclerView recyclerViewTrailers, recyclerViewReviews;
    private RequestQueue requestQueue;

    private static final int DEFAULT_MOVIE_ID = -1;
    private static final String TOTAL_Rating = "/10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        recyclerViewReviews = findViewById(R.id.recyclerView_reviews);
        recyclerViewTrailers = findViewById(R.id.recyclerView_trailers);

        trailersNames = new ArrayList<>();
        trailersImages = new ArrayList<>();
        keys = new ArrayList<>();

        authors = new ArrayList<>();
        contents = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        errorReview = findViewById(R.id.noFoundReviewsText);
        errorTrailer = findViewById(R.id.noFoundTrailersText);

        redFavorite = findViewById(R.id.redFavorite);
        whiteFavorite = findViewById(R.id.whiteFavorite);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Intent intent = getIntent();
        if (intent == null) {
            errorMassage();
            return;
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.isEmpty()) {
            errorMassage();
            return;
        }

        String overview = bundle.getString(AppConstants.mOverviewKey);
        String title = bundle.getString(AppConstants.mTitleKey);
        String image = bundle.getString(AppConstants.mPosterKey);
        float rate = bundle.getFloat(AppConstants.mRateKey);

        String releaseDate = bundle.getString(AppConstants.mReleaseDateKey);
        Date mReleaseDate = null;
        if (releaseDate != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                mReleaseDate = dateFormat.parse(releaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        int id = bundle.getInt(AppConstants.mIdKey, DEFAULT_MOVIE_ID);
        Movie movie;
        movie = new Movie(id, title, overview, mReleaseDate, image, rate);
        mMovie = movie;
        populateUI(overview, title, image, rate, releaseDate);
        populateTrailers(id);
        populateReviews(id);

        redFavorite.setOnClickListener(this);
        whiteFavorite.setOnClickListener(this);
        checkFavorite(movie);
    }

    public void errorMassage() {
        finish();
        Toast.makeText(getApplicationContext(), R.string.error_massage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.redFavorite:
                setFavorite(false);
                viewModel.delete(mMovie);
                break;
            case R.id.whiteFavorite:
                setFavorite(true);
                viewModel.insert(mMovie);
                break;
        }
    }

    public void checkFavorite(Movie movie) {
        if (viewModel.selectMovieById(movie.getId()) != null) {
            setFavorite(true);
        } else
            setFavorite(false);
    }

    public void setFavorite(Boolean flag) {
        if (flag == true) {
            redFavorite.setVisibility(View.VISIBLE);
            whiteFavorite.setVisibility(View.GONE);
        } else {
            redFavorite.setVisibility(View.GONE);
            whiteFavorite.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void populateUI(String overview, String title, String image, float rate, String releaseDate) {
        mOverviewTV = findViewById(R.id.movieOverview);
        if (overview != null || !overview.equals("")) {
            mOverviewTV.setText(overview);
        } else {
            mOverviewTV.setText(R.string.error_overview);
            mOverviewTV.setTextColor(R.color.colorError);
        }

        mPosterIV = findViewById(R.id.moviePoster);
        Picasso.with(getApplicationContext())
                .load(AppConstants.imageBaseUrl + AppConstants.imageSize + image)
                .placeholder(R.drawable.loadimage)
                .error(R.drawable.error)
                .into(mPosterIV);


        mRatingTV = findViewById(R.id.movieRating);
        mRatingTV.setText(String.valueOf(rate) + TOTAL_Rating);

        mReleaseDateTV = findViewById(R.id.movieReleaseDate);
        if (releaseDate != null || !overview.equals("")) {
            mReleaseDateTV.setText(releaseDate);
        } else {
            mReleaseDateTV.setText(R.string.error_release_date);
            mReleaseDateTV.setTextSize(12);
            mReleaseDateTV.setTextColor(R.color.colorError);
        }


        mTitleTV = findViewById(R.id.movieTitle);
        if (title != null || !overview.equals("")) {
            mTitleTV.setText(title);
            setTitle(title);
        } else {
            mTitleTV.setText(R.string.error_title);
            mReleaseDateTV.setTextSize(10);
            mReleaseDateTV.setTextColor(R.color.colorError);
        }

    }


    public void populateTrailers(int id) {
        String stringUrl = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=d356736ae8fd73791fa8988af49d36e8";
        JsonObjectRequest objectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, stringUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject trailer = jsonArray.getJSONObject(i);
                        String id = trailer.getString("id");
                        String key = trailer.getString("key");
                        String name = trailer.getString("name");
                        trailersNames.add(i, name);
                        String imagePath = "https://img.youtube.com/vi/" + key + "/0.jpg";
                        trailersImages.add(i, imagePath);
                        keys.add(i, key);
                    }
                    if (trailersNames.isEmpty() && trailersImages.isEmpty()) {
                        recyclerViewTrailers.setVisibility(View.INVISIBLE);
                        errorTrailer.setVisibility(View.VISIBLE);

                    } else {
                        recyclerViewTrailers.setVisibility(View.VISIBLE);
                        errorTrailer.setVisibility(View.INVISIBLE);
                        setRecyclerViewTrailers();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(objectRequest);
    }

    public void populateReviews(int id) {
        String stringUrl = "https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=d356736ae8fd73791fa8988af49d36e8";
        JsonObjectRequest request2 = new JsonObjectRequest(com.android.volley.Request.Method.GET, stringUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject review = jsonArray.getJSONObject(i);
                        String author = review.getString("author");
                        String content = review.getString("content");
                        authors.add(i, author);
                        contents.add(i, content);
                    }
                    if (authors.isEmpty() && contents.isEmpty()) {
                        recyclerViewReviews.setVisibility(View.INVISIBLE);
                        errorReview.setVisibility(View.VISIBLE);
                    } else {
                        recyclerViewReviews.setVisibility(View.VISIBLE);
                        errorReview.setVisibility(View.INVISIBLE);
                        setRecyclerViewReviews();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request2);
    }

    public void setRecyclerViewTrailers() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewTrailers.setLayoutManager(manager);
        MyRecyclerTrailersAdapter adapter = new MyRecyclerTrailersAdapter(MovieDetails.this, trailersNames, trailersImages, keys);
        recyclerViewTrailers.setAdapter(adapter);
    }

    public void setRecyclerViewReviews() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewReviews.setLayoutManager(manager);
        MyRecyclerReviewsAdapter adapter = new MyRecyclerReviewsAdapter(MovieDetails.this, authors, contents);
        recyclerViewReviews.setAdapter(adapter);
    }

}
