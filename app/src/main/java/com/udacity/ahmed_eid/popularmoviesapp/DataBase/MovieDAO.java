package com.udacity.ahmed_eid.popularmoviesapp.DataBase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.udacity.ahmed_eid.popularmoviesapp.model.Movie;

import java.util.List;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> loadAllMovies();

    @Query("SELECT * FROM movie WHERE id= :id LIMIT 1")
    Movie selectMovieById(int id);

    @Insert
    void insertMovie(Movie movie);

    // @Update(onConflict = OnConflictStrategy.REPLACE)
    //void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
