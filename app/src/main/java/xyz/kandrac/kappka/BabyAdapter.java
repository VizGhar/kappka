package xyz.kandrac.kappka;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import xyz.kandrac.kappka.utils.DisplayUtils;

/**
 * Adapter for baby activity
 * <p>
 * Created by jan on 9.2.2017.
 */
public class BabyAdapter extends RecyclerView.Adapter<BabyAdapter.ViewHolder> {

    private Context context;

    public BabyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.time.setText("18:35");
        holder.description.setText(position + " desc");

        switch (position % 5) {
            case 0:
                holder.indicator.setBackgroundColor(DisplayUtils.getColor(context, R.color.worst));
                break;
            case 1:
                holder.indicator.setBackgroundColor(DisplayUtils.getColor(context, R.color.bad));
                break;
            case 2:
                holder.indicator.setBackgroundColor(DisplayUtils.getColor(context, R.color.average));
                break;
            case 3:
                holder.indicator.setBackgroundColor(DisplayUtils.getColor(context, R.color.good));
                break;
            case 4:
                holder.indicator.setBackgroundColor(DisplayUtils.getColor(context, R.color.best));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView time;
        private TextView description;
        private View indicator;

        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.item_time);
            description = (TextView) itemView.findViewById(R.id.item_desc);
            indicator = itemView.findViewById(R.id.item_indicator);
        }
    }
}
