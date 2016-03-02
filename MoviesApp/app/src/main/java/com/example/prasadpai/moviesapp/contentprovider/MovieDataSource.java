package com.example.prasadpai.moviesapp.contentprovider;

/**
 * Created by prasadpai on 27/02/16.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.prasadpai.moviesapp.contentprovider.ContentProviderDb;
import com.example.prasadpai.moviesapp.contentprovider.MySQLiteHelper;
import com.example.prasadpai.moviesapp.models.Movie;

public class MovieDataSource {

    Uri contentUri;

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_POSTER,
            MySQLiteHelper.COLUMN_RATINGS,
            MySQLiteHelper.COLUMN_RELEASEDATE,
            MySQLiteHelper.COLUMN_OVERVIEW,
            MySQLiteHelper.COLUMN_POPULARITY};

    public MovieDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        contentUri = Uri.withAppendedPath(ContentProviderDb.CONTENT_URI, MySQLiteHelper.TABLE_MOVIE);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public void insertFilm(Context context, int id,String comment, String poster, double ratings, String releaseDate, double popularity, String overview) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ID, id);
        values.put(MySQLiteHelper.COLUMN_NAME, comment);
        values.put(MySQLiteHelper.COLUMN_POSTER, poster);
        values.put(MySQLiteHelper.COLUMN_RATINGS, ratings);
        values.put(MySQLiteHelper.COLUMN_RELEASEDATE, releaseDate);
        values.put(MySQLiteHelper.COLUMN_OVERVIEW, overview);
        values.put(MySQLiteHelper.COLUMN_POPULARITY, popularity);
        context.getContentResolver().insert(contentUri, values);

    }

    public void deleteFilm(Context context, int filmid)
    {
        context.getContentResolver().delete(contentUri, MySQLiteHelper.COLUMN_ID
                + " = " + filmid, null);
    }



    public ArrayList<Movie> getAllFilms(Context context) {
        ArrayList<Movie> movies = new ArrayList<>();


        Cursor cursor =  context.getContentResolver().query(contentUri, allColumns, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie comment = cursorToMovie(cursor);
            movies.add(comment);
            cursor.moveToNext();
        }
        cursor.close();
        return movies;
    }


    private Movie cursorToMovie(Cursor cursor) {
        Movie movie = new Movie();
        movie.setId(cursor.getInt(0));
        movie.setTitle(cursor.getString(1));
        movie.setPoster_path(cursor.getString(2));
        movie.setVote_average(Double.valueOf(cursor.getString(3)));
        movie.setRelease_date(cursor.getString(4));
        movie.setOverview(cursor.getString(5));
        movie.setPopularity(Double.valueOf(cursor.getString(6)));
        return movie;
    }
}

