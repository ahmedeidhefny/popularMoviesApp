package com.udacity.ahmed_eid.popularmoviesapp.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.udacity.ahmed_eid.popularmoviesapp.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract  class MovieRoomDB extends RoomDatabase{

    public static final String DATABASE_NAME = "movies_database";

    public abstract MovieDAO movieDAO() ;

    private static volatile MovieRoomDB mINSTANCE;

    public static MovieRoomDB getDatabase (final Context context) {
        if (mINSTANCE == null) {
            synchronized (MovieRoomDB.class) {
                if (mINSTANCE == null) {
                    //Create DataBase Using Room
                    mINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDB.class, MovieRoomDB.DATABASE_NAME)
                            .build();
                }
            }
        }
        return mINSTANCE;
    }

}
