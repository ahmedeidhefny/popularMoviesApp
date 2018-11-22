package com.udacity.ahmed_eid.popularmoviesapp.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


import com.udacity.ahmed_eid.popularmoviesapp.DataBase.MainViewModel;
import com.udacity.ahmed_eid.popularmoviesapp.Adapters.MyGridAdapter;
import com.udacity.ahmed_eid.popularmoviesapp.R;
import com.udacity.ahmed_eid.popularmoviesapp.model.Movie;
import com.udacity.ahmed_eid.popularmoviesapp.utilities.AppConstants;
import com.udacity.ahmed_eid.popularmoviesapp.utilities.JsonUtils;
import com.udacity.ahmed_eid.popularmoviesapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainDiscoverActivity extends AppCompatActivity {
    private GridView gridView;
    private ArrayList<Movie> movies;
    private Spinner spinner;

    private TextView tv_errorMassage;
    private ProgressBar pb_loadingIndicator;

    private ConnectivityManager conMgr;

    private MainViewModel viewModel;

    private final String[] sortedBy = {AppConstants.popularMovies, AppConstants.ratingMovies, AppConstants.favoriteMovies};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_discover);

        conMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        movies = new ArrayList<>();
        gridView = findViewById(R.id.gridView);
        tv_errorMassage = findViewById(R.id.error_massage_display);
        pb_loadingIndicator = findViewById(R.id.pb_loading_indicator);

        setSpinner();
    }

    public void setSpinner() {
        spinner = findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, sortedBy);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) == AppConstants.popularMovies) {
                    fetchDataFromAPI(AppConstants.popularMoviesUrl);
                } else if (parent.getItemAtPosition(position) == AppConstants.ratingMovies) {
                    fetchDataFromAPI(AppConstants.ratingMoviesUrl);
                } else if (parent.getItemAtPosition(position) == AppConstants.favoriteMovies) {
                    showFavoriteMovies();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void showFavoriteMovies() {
        viewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies1) {
                ArrayList<Movie> movies = new ArrayList<>();
                movies.addAll(movies1);
                if (movies.isEmpty()) {
                    showErrorMassage(AppConstants.NotFavoritesMassage);
                } else {
                    showJsonDataView();
                    MyGridAdapter adapter = new MyGridAdapter(getApplicationContext(), movies);
                    gridView.setAdapter(adapter);
                }

            }
        });
    }

    public void fetchDataFromAPI(String queryUrl) {
        if (conMgr.getActiveNetworkInfo() == null
                || !conMgr.getActiveNetworkInfo().isAvailable()
                || !conMgr.getActiveNetworkInfo().isConnected()) {

            Log.i(AppConstants.jsonResultsTag, AppConstants.NetworkMassage);
            pb_loadingIndicator.setVisibility(View.INVISIBLE);
            showErrorMassage(AppConstants.NetworkMassage);
        } else {
            URL MoviesURL = NetworkUtils.buildUrl(queryUrl);
            new moviesBackGroundTask().execute(MoviesURL);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelectedId = item.getItemId();
        switch (itemSelectedId) {
            case R.id.app_bar_popular:
                fetchDataFromAPI(AppConstants.popularMoviesUrl);
                break;
            case R.id.app_bar_highest_rate:
                fetchDataFromAPI(AppConstants.ratingMoviesUrl);
                break;
            case R.id.app_bar_favorite:
                showFavoriteMovies();
                break;
            case R.id.app_bar_close:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void showErrorMassage(String massage) {
        gridView.setVisibility(View.INVISIBLE);
        tv_errorMassage.setVisibility(View.VISIBLE);
        tv_errorMassage.setText(massage);
    }

    public void showJsonDataView() {
        gridView.setVisibility(View.VISIBLE);
        tv_errorMassage.setVisibility(View.INVISIBLE);
    }

    //<parameters ,progress ,result>
    public class moviesBackGroundTask extends AsyncTask<URL, Void, ArrayList<Movie>> {
        @Override
        protected void onPreExecute() {
            pb_loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String JSONResults = null;
            try {
                JSONResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (JSONResults == null || JSONResults.equals("")) {
                Log.i(AppConstants.jsonResultsTag, "null");
            } else {
                try {
                    movies = JsonUtils.parseMoviesJson(JSONResults);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            pb_loadingIndicator.setVisibility(View.INVISIBLE);
            if (movies.isEmpty()) {
                showErrorMassage(AppConstants.errorMassage);
            } else {
                showJsonDataView();
                MyGridAdapter adapter = new MyGridAdapter(getApplicationContext(), movies);
                gridView.setAdapter(adapter);
            }

        }

    }

}
