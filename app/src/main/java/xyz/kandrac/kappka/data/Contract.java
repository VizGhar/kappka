package xyz.kandrac.kappka.data;

import android.net.Uri;
import android.provider.BaseColumns;

import xyz.kandrac.kappka.BuildConfig;

/**
 * Created by jan on 9.2.2017.
 */

public final class Contract {

    private Contract() {
    }

    // Database Columns
    public interface ActivityColumns {
        String ACTIVITY_ID = BaseColumns._ID;
        String ACTIVITY_TYPE = "activity_type";
        String ACTIVITY_SCORE = "activity_score";
        String ACTIVITY_TIME_FROM = "activity_from";
        String ACTIVITY_TIME_TO = "activity_to";
        String ACTIVITY_DESCRIPTION = "activity_description";
    }

    // Base URI specification (authority and its URI representation)
    public static final String CONTENT_AUTHORITY = BuildConfig.DATABASE_AUTHORITY;
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // URI Paths
    public static final String PATH_ACTIVITIES = "activities";


    public static class Activities implements ActivityColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACTIVITIES).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.activities";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.activities";

        public static final String DEFAULT_SORT = ACTIVITY_TIME_FROM + " ASC";

        public static Uri buildActivityUri(long activityId) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(activityId)).build();
        }

        public static long getActivityId(Uri uri) {
            return Long.parseLong(uri.getLastPathSegment());
        }
    }
}
