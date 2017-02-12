package xyz.kandrac.kappka;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

import xyz.kandrac.kappka.data.Contract;
import xyz.kandrac.kappka.data.Contract.Activities.ActivityType;

import static android.view.View.GONE;

/**
 * Created by jan on 9.2.2017.
 */
public class AddFragment extends DialogFragment implements View.OnClickListener {

    public static final String ARGUMENT_ACTIVITY_TYPE = "type";

    EditText description;
    Spinner score;
    Button timeFrom;
    Button timeTo;
    Button save;

    int timeFromHour;
    int timeFromMinute;
    int timeToHour;
    int timeToMinute;
    int type;

    public static AddFragment getInstance(@ActivityType int activityType) {
        AddFragment result = new AddFragment();
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_ACTIVITY_TYPE, activityType);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(ARGUMENT_ACTIVITY_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_add, container, false);
        description = (EditText) result.findViewById(R.id.description);
        score = (Spinner) result.findViewById(R.id.score);
        timeFrom = (Button) result.findViewById(R.id.time_from);
        timeTo = (Button) result.findViewById(R.id.time_to);
        save = (Button) result.findViewById(R.id.save);
        return result;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        timeFrom.setOnClickListener(this);
        timeTo.setOnClickListener(this);
        save.setOnClickListener(this);

        if (type == Contract.Activities.ACTIVITY_EAT || type == Contract.Activities.ACTIVITY_POOP) {
            timeTo.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View view) {
        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);

        switch (view.getId()) {
            case R.id.time_from:
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                        timeFromHour = hours;
                        timeFromMinute = minutes;
                    }
                }, hour, minute, DateFormat.is24HourFormat(getActivity())).show();
                break;
            case R.id.time_to:
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                        timeToHour = hours;
                        timeToMinute = minutes;
                    }
                }, hour, minute, DateFormat.is24HourFormat(getActivity())).show();
                break;
            case R.id.save:
                final Calendar fromCalendar = Calendar.getInstance();
                fromCalendar.set(Calendar.HOUR_OF_DAY, timeFromHour);
                fromCalendar.set(Calendar.MINUTE, timeFromMinute);

                final Calendar toCalendar = Calendar.getInstance();
                toCalendar.set(Calendar.HOUR_OF_DAY, timeToHour);
                toCalendar.set(Calendar.MINUTE, timeToMinute);

                ContentValues cv = new ContentValues();
                cv.put(Contract.ActivityColumns.ACTIVITY_DESCRIPTION, description.getText().toString());
                cv.put(Contract.ActivityColumns.ACTIVITY_SCORE, score.getSelectedItemPosition());
                cv.put(Contract.ActivityColumns.ACTIVITY_TIME_FROM, fromCalendar.getTimeInMillis());
                if (type == Contract.Activities.ACTIVITY_SLEEP)
                    cv.put(Contract.ActivityColumns.ACTIVITY_TIME_TO, toCalendar.getTimeInMillis());
                cv.put(Contract.ActivityColumns.ACTIVITY_TYPE, type);
                getActivity().getContentResolver().insert(Contract.Activities.CONTENT_URI, cv);
                dismiss();
                break;
        }
    }
}
