package xyz.kandrac.kappka.mvp.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite database representation for this application
 * Created by VizGhar on 9.8.2015.
 */
public class Database extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "library.db";
    private static final int DATABASE_VERSION = 9;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public interface Tables {
        String ACTIVITIES = "books";
    }

    private static final String ACTIVITIES_CREATE_TABLE =
            "CREATE TABLE " + Tables.ACTIVITIES + " (" +
                    Contract.Activities.ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Contract.Activities.ACTIVITY_SCORE + " INTEGER," +
                    Contract.Activities.ACTIVITY_TYPE + " INTEGER," +
                    Contract.Activities.ACTIVITY_TIME_FROM + " INTEGER," +
                    Contract.Activities.ACTIVITY_TIME_TO + " INTEGER," +
                    Contract.Activities.ACTIVITY_DESCRIPTION + " TEXT)";

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ACTIVITIES_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
