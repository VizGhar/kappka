package xyz.kandrac.kappka.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Content provider for all database items.
 * Created by VizGhar on 9.8.2015.
 */
public class DatabaseProvider extends ContentProvider {

    private static final String LOG_TAG = DatabaseProvider.class.getName();

    private Database databaseHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    // Everything from books (SELECT, INSERT, UPDATE, DELETE)
    public static final int ACTIVITIES = 100;
    public static final int ACTIVITY = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, "activities", ACTIVITIES);
        uriMatcher.addURI(authority, "activities/#", ACTIVITY);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        final Context context = getContext();
        databaseHelper = new Database(context);
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ACTIVITIES:
                return Contract.Activities.CONTENT_TYPE;
            case ACTIVITY:
                return Contract.Activities.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case ACTIVITIES:
                qb.setTables(Database.Tables.ACTIVITIES);
                sortOrder = sortOrder == null ? Contract.Activities.DEFAULT_SORT : sortOrder;
                break;
            case ACTIVITY:
                qb.setTables(Database.Tables.ACTIVITIES);
                selection = Contract.ActivityColumns.ACTIVITY_ID + " = ?";
                selectionArgs = new String[]{Long.toString(Contract.Activities.getActivityId(uri))};
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        Context context = getContext();
        if (context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }
        return cursor;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Uri resultUrl;

        switch (uriMatcher.match(uri)) {
            case ACTIVITIES: {
                long result = db.insert(Database.Tables.ACTIVITIES, null, values);
                resultUrl = Contract.Activities.buildActivityUri(result);
                getContext().getContentResolver().notifyChange(Contract.Activities.CONTENT_URI, null);
                return resultUrl;
            }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case ACTIVITY: {
                long id = Contract.Activities.getActivityId(uri);
                count = db.delete(Database.Tables.ACTIVITIES, Contract.Activities.ACTIVITY_ID + " = ?", new String[]{Long.toString(id)});

                getContext().getContentResolver().notifyChange(Contract.Activities.CONTENT_URI, null);
                break;
            }
            case ACTIVITIES: {
                count = db.delete(Database.Tables.ACTIVITIES, selection, selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case ACTIVITIES: {
                count = db.update(Database.Tables.ACTIVITIES, values, selection, selectionArgs);
                break;
            }
            case ACTIVITY: {
                long id = Contract.Activities.getActivityId(uri);
                count = db.update(Database.Tables.ACTIVITIES, values, Contract.ActivityColumns.ACTIVITY_ID + " = ?", new String[]{Long.toString(id)});
                getContext().getContentResolver().notifyChange(Contract.Activities.CONTENT_URI, null);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
