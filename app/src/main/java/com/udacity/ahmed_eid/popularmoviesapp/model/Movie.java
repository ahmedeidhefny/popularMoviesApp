package com.udacity.ahmed_eid.popularmoviesapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.udacity.ahmed_eid.popularmoviesapp.DataBase.DateConverter;

import java.util.Date;

@TypeConverters(DateConverter.class)
@Entity(tableName = "movie")
public class Movie {

    @PrimaryKey
    private int id;
    private String title;
    @ColumnInfo(name = "over_view")
    private String overview;
    private float rate;
    @ColumnInfo(name = "release_date")
    private Date releaseDate;
    @ColumnInfo(name = "poster_image")
    private String posterImage;

    public Movie(int id, String title, String overview, Date releaseDate, String posterImage, float rate) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterImage = posterImage;
        this.rate = rate;

    }

    public float getRate() {
        return rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getPosterImage() {
        return posterImage;
    }

}
