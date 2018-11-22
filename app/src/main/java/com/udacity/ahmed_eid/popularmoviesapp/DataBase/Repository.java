package com.udacity.ahmed_eid.popularmoviesapp.DataBase;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.udacity.ahmed_eid.popularmoviesapp.model.Movie;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class Repository {

    LiveData<List<Movie>> movies ;
    MovieDAO movieDAO ;

    public Repository(@NonNull Application application) {
       MovieRoomDB db = MovieRoomDB.getDatabase(application);
       movieDAO = db.movieDAO();
       movies = movieDAO.loadAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return movies;
    }

    public void insert(Movie movie) {
        new insertAsyncTask(movieDAO).execute(movie);
    }

    public void delete(Movie movie) {
        new deleteAsyncTask(movieDAO).execute(movie);
    }

    public Movie selectMovieById(int id) {
        try {
            return new getMovieAsyncTask(movieDAO).execute(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }



    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDAO mAsyncTaskDao;
        insertAsyncTask(MovieDAO mDao) {
            mAsyncTaskDao = mDao;
        }
        @Override
        protected Void doInBackground(Movie... params) {
            mAsyncTaskDao.insertMovie(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDAO mAsyncTaskDao;
        deleteAsyncTask(MovieDAO mDao) {
            mAsyncTaskDao = mDao;
        }
        @Override
        protected Void doInBackground(Movie... params) {
            mAsyncTaskDao.deleteMovie(params[0]);
            return null;
        }
    }

    private static class getMovieAsyncTask extends AsyncTask<Integer, Void, Movie> {
        private MovieDAO mAsyncTaskDao;
        getMovieAsyncTask(MovieDAO mDao) {
            mAsyncTaskDao = mDao;
        }
        @Override
        protected Movie doInBackground(final Integer... params) {
            return mAsyncTaskDao.selectMovieById(params[0]);
        }
    }


}
