package com.example.prasadpai.moviesapp.sql;

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

import com.example.prasadpai.moviesapp.models.Film;

public class MovieDataSource {

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
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Film createFilm(int id,String comment, String poster, double ratings, String releaseDate, double popularity, String overview) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ID, id);
        values.put(MySQLiteHelper.COLUMN_NAME, comment);
        values.put(MySQLiteHelper.COLUMN_POSTER, poster);
        values.put(MySQLiteHelper.COLUMN_RATINGS, ratings);
        values.put(MySQLiteHelper.COLUMN_RELEASEDATE, releaseDate);
        values.put(MySQLiteHelper.COLUMN_OVERVIEW, overview);
        values.put(MySQLiteHelper.COLUMN_POPULARITY, popularity);

        long insertId = database.insert(MySQLiteHelper.TABLE_MOVIE, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MOVIE,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        Film newFilm = cursorToComment(cursor);
        cursor.close();
        return newFilm;
    }

    public void deleteComment(Film film) {
        long id = film.getId();
        System.out.println("Film deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_MOVIE, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<Film>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_MOVIE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Film comment = cursorToComment(cursor);
            films.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return films;
    }

    private Film cursorToComment(Cursor cursor) {
        Film film = new Film();
        film.setId(cursor.getInt(0));
        film.setTitle(cursor.getString(1));
        film.setPoster_path(cursor.getString(2));
        film.setVote_average(Double.valueOf(cursor.getString(3)));
        film.setRelease_date(cursor.getString(4));
        film.setOverview(cursor.getString(5));
        film.setPopularity(Double.valueOf(cursor.getString(6)));
        return film;
    }
}

