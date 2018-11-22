package com.udacity.ahmed_eid.popularmoviesapp.DataBase;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.udacity.ahmed_eid.popularmoviesapp.model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    LiveData<List<Movie>> movies;
    Repository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        movies = repository.getAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return movies;
    }

    public void insert(Movie movie) {
        repository.insert(movie);
    }

    public void delete(Movie movie) {
        repository.delete(movie);
    }

    public Movie selectMovieById(int id) {
        return repository.selectMovieById(id);
    }
}
