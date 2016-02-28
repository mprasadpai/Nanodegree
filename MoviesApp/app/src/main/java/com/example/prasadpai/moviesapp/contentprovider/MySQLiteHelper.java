package com.example.prasadpai.moviesapp.contentprovider;

/**
 * Created by prasadpai on 27/02/16.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_MOVIE = "movie";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_POSTER = "poster";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_RATINGS = "ratings";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_RELEASEDATE = "releaseDate";

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_MOVIE + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_POPULARITY + " real not null, "
            + COLUMN_RATINGS + " real not null, "
            + COLUMN_OVERVIEW + " text not null, "
            + COLUMN_RELEASEDATE + " text not null, "
            + COLUMN_POSTER+ " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        onCreate(db);
    }

}
