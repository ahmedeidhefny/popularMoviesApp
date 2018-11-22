package com.udacity.ahmed_eid.popularmoviesapp.utilities;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {

    public static URL buildUrl(String queryOrderBy) {
        Uri BuildUri = Uri.parse(queryOrderBy).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(BuildUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /*
    Create a new Scanner , set the delimiter to “\A” which is actually a regex for the
    beginning of the input. next() will then return from the beginning all the way to the end of
    the file.This can be compressed into one line:
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext())
                return scanner.next();
            else
                return null;

        } finally {
            urlConnection.disconnect();
        }
    }


}
