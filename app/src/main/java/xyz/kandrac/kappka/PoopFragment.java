package xyz.kandrac.kappka;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.kandrac.kappka.data.Contract;

/**
 * Created by jan on 9.2.2017.
 */
public class PoopFragment extends Fragment {

    RecyclerView recyclerView;

    public static PoopFragment getInstance() {
        return new PoopFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_poop, container, false);
        recyclerView = (RecyclerView) result.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new BabyAdapter(2, getActivity(), Contract.ActivityColumns.ACTIVITY_TYPE + "=?", new String[]{"2"}));
        return result;
    }
}
