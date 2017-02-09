package xyz.kandrac.kappka;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import xyz.kandrac.kappka.data.Contract;
import xyz.kandrac.kappka.utils.DisplayUtils;

/**
 * Adapter for baby activity
 * <p>
 * Created by jan on 9.2.2017.
 */
public class BabyAdapter extends RecyclerView.Adapter<BabyAdapter.ViewHolder> implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());

    private Activity activity;

    private int mLoaderId;                          // Loader id to be used with queries
    private Cursor mCursor;                         // Cursor with current data

    private String selectionString = null;
    private String[] selectionArguments = null;


    public BabyAdapter(int loaderId, Activity activity) {
        this.activity = activity;
        mLoaderId = loaderId;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // handle only set loader, because you can receive other loader calls from same activity
        if (id == mLoaderId) {
            return new CursorLoader(
                    activity,                          // activity required for cursor loading
                    Contract.Activities.CONTENT_URI,    //
                    null,                               // static projection columns, cannot be edited
                    selectionString,               // static selection string from initialization
                    selectionArguments,            // dynamically updated selection arguments
                    null);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // handle only set loader, because you can receive other loader calls from same activity
        if (loader.getId() == mLoaderId) {
            mCursor = data;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(activity).inflate(R.layout.list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String description = mCursor.getString(mCursor.getColumnIndex(Contract.ActivityColumns.ACTIVITY_DESCRIPTION));
        int type = mCursor.getType(mCursor.getColumnIndex(Contract.ActivityColumns.ACTIVITY_TYPE));
        int score = mCursor.getType(mCursor.getColumnIndex(Contract.ActivityColumns.ACTIVITY_SCORE));
        long from = mCursor.getType(mCursor.getColumnIndex(Contract.ActivityColumns.ACTIVITY_TIME_FROM));
        long to = mCursor.getType(mCursor.getColumnIndex(Contract.ActivityColumns.ACTIVITY_TIME_TO));

        holder.description.setText(description);

        holder.time.setText(to == 0
                ? activity.getString(R.string.time_format, TIME_FORMAT.format(new Date(from)))
                : activity.getString(R.string.time_format_from_to, TIME_FORMAT.format(new Date(from)), TIME_FORMAT.format(new Date(to))));

        // score set
        switch (score) {
            case 0:
                holder.indicator.setBackgroundColor(DisplayUtils.getColor(activity, R.color.worst));
                break;
            case 1:
                holder.indicator.setBackgroundColor(DisplayUtils.getColor(activity, R.color.bad));
                break;
            case 2:
                holder.indicator.setBackgroundColor(DisplayUtils.getColor(activity, R.color.average));
                break;
            case 3:
                holder.indicator.setBackgroundColor(DisplayUtils.getColor(activity, R.color.good));
                break;
            case 4:
                holder.indicator.setBackgroundColor(DisplayUtils.getColor(activity, R.color.best));
                break;
        }

        switch (type) {
            case 0:
                holder.image.setImageResource(R.drawable.ic_spoon);
                break;
            case 1:
                holder.image.setImageResource(R.drawable.ic_sleep);
                break;
            case 2:
                holder.image.setImageResource(R.drawable.ic_poop);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView time;
        private TextView description;
        private View indicator;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.item_time);
            description = (TextView) itemView.findViewById(R.id.item_desc);
            indicator = itemView.findViewById(R.id.item_indicator);
            image = (ImageView) itemView.findViewById(R.id.item_image);
        }
    }
}
