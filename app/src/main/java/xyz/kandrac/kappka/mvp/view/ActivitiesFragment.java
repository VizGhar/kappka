package xyz.kandrac.kappka.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.kandrac.kappka.BabyAdapter;
import xyz.kandrac.kappka.R;
import xyz.kandrac.kappka.mvp.model.Contract.Activities.ActivityType;

/**
 * Created by jan on 9.2.2017.
 */
public class ActivitiesFragment extends Fragment {

    RecyclerView recyclerView;
    BabyAdapter adapter;

    public static ActivitiesFragment getInstance() {
        return new ActivitiesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_activities, container, false);
        adapter = new BabyAdapter(0, getActivity());
        recyclerView = (RecyclerView) result.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return result;
    }

    public void setDateRange(long dateFrom, long dateTo) {
        adapter.setDateRange(dateFrom, dateTo);
    }

    public void setType(@ActivityType int type) {
        adapter.setType(type);
    }

    public void clearType() {
        adapter.clearType();
    }
}
