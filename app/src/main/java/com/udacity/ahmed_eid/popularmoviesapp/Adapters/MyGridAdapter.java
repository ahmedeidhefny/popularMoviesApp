package com.udacity.ahmed_eid.popularmoviesapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.ahmed_eid.popularmoviesapp.Activities.MovieDetails;
import com.udacity.ahmed_eid.popularmoviesapp.R;
import com.udacity.ahmed_eid.popularmoviesapp.model.Movie;
import com.udacity.ahmed_eid.popularmoviesapp.utilities.AppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyGridAdapter extends ArrayAdapter<Movie> {


    private final Context context;

    public MyGridAdapter(Context mContext, ArrayList<Movie> mMovies) {
        super(mContext, 0, mMovies);
        this.context = mContext;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View myView = LayoutInflater.from(context)
                .inflate(R.layout.grid_item, parent, false);

        final Movie movie = getItem(position);
        ImageView poster = myView.findViewById(R.id.moviePosterImage);
        String imagePath = movie.getPosterImage();
        Picasso.with(context)
                .load(AppConstants.imageBaseUrl + AppConstants.imageSize + imagePath)
                .placeholder(R.drawable.loadimage)
                .error(R.drawable.error)
                .into(poster);

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMovieDetails = new Intent(context, MovieDetails.class);

                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.mIdKey, movie.getId());
                bundle.putString(AppConstants.mOverviewKey, movie.getOverview());
                bundle.putFloat(AppConstants.mRateKey, movie.getRate());
                bundle.putString(AppConstants.mTitleKey, movie.getTitle());
                bundle.putString(AppConstants.mPosterKey, movie.getPosterImage());
                Date releaseDate = movie.getReleaseDate();
                String mReleaseDate = null;
                if (releaseDate != null) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    mReleaseDate = format.format(releaseDate);
                }
                bundle.putString(AppConstants.mReleaseDateKey, mReleaseDate);

                goToMovieDetails.putExtras(bundle);
                context.startActivity(goToMovieDetails);
            }
        });
        return myView;
    }
}
