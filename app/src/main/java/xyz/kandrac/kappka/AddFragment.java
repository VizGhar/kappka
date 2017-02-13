package xyz.kandrac.kappka;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import xyz.kandrac.kappka.data.Contract;
import xyz.kandrac.kappka.data.Contract.Activities.ActivityType;
import xyz.kandrac.kappka.utils.DateUtils;

import static android.view.View.GONE;

/**
 * Created by jan on 9.2.2017.
 */
public class AddFragment extends DialogFragment implements View.OnClickListener {

    public static final String ARGUMENT_ACTIVITY_TYPE = "type";
    public static final String ARGUMENT_TIME = "time";


    TextView activityName;
    EditText description;
    Spinner score;
    Button timeFrom;
    Button timeTo;
    Button save;

    TextView activity_start_title;
    TextView activity_end_title;
    ImageButton activity_start_minus;
    ImageButton activity_start_plus;
    ImageButton activity_end_plus;
    ImageButton activity_end_minus;

    Calendar timeFromCalendar;
    Calendar timeToCalendar;

    int type;

    public static AddFragment getInstance(@ActivityType int activityType, long displayTime) {
        AddFragment result = new AddFragment();
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_ACTIVITY_TYPE, activityType);
        args.putLong(ARGUMENT_TIME, displayTime);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(ARGUMENT_ACTIVITY_TYPE);

        long time = getArguments().getLong(ARGUMENT_TIME);
        Calendar display = Calendar.getInstance();
        display.setTimeInMillis(time);

        timeFromCalendar = Calendar.getInstance();
        timeToCalendar = Calendar.getInstance();

        timeFromCalendar.set(Calendar.DAY_OF_MONTH, display.get(Calendar.DAY_OF_MONTH));
        timeFromCalendar.set(Calendar.MONTH, display.get(Calendar.MONTH));
        timeFromCalendar.set(Calendar.YEAR, display.get(Calendar.YEAR));

        timeToCalendar.setTimeInMillis(timeFromCalendar.getTimeInMillis());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_add, container, false);
        activityName = (TextView) result.findViewById(R.id.activity_name);
        description = (EditText) result.findViewById(R.id.description);
        score = (Spinner) result.findViewById(R.id.score);
        timeFrom = (Button) result.findViewById(R.id.time_from);
        timeTo = (Button) result.findViewById(R.id.time_to);
        activity_start_title = (TextView) result.findViewById(R.id.activity_start_title);
        activity_end_title = (TextView) result.findViewById(R.id.activity_end_title);
        activity_start_minus = (ImageButton) result.findViewById(R.id.activity_start_minus);
        activity_start_plus = (ImageButton) result.findViewById(R.id.activity_start_plus);
        activity_end_plus = (ImageButton) result.findViewById(R.id.activity_end_plus);
        activity_end_minus = (ImageButton) result.findViewById(R.id.activity_end_minus);
        save = (Button) result.findViewById(R.id.save);
        return result;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        activityName.setText(getActivityName(type, getActivity()));
        timeFrom.setOnClickListener(this);
        timeTo.setOnClickListener(this);
        save.setOnClickListener(this);

        activity_end_minus.setOnClickListener(this);
        activity_end_plus.setOnClickListener(this);
        activity_start_minus.setOnClickListener(this);
        activity_start_plus.setOnClickListener(this);

        if (type == Contract.Activities.ACTIVITY_EAT || type == Contract.Activities.ACTIVITY_POOP) {
            timeTo.setVisibility(GONE);
            activity_end_minus.setVisibility(GONE);
            activity_end_plus.setVisibility(GONE);
            activity_end_title.setVisibility(GONE);
            activity_start_title.setText(R.string.activity_start_hint_variant);
        }

        resetFromButton();
        resetToButton();
    }

    private static String getActivityName(@ActivityType int type, @NonNull Context context) {
        switch (type) {
            case Contract.Activities.ACTIVITY_EAT:
                return context.getString(R.string.main_tab_eat);
            case Contract.Activities.ACTIVITY_POOP:
                return context.getString(R.string.main_tab_poop);
            case Contract.Activities.ACTIVITY_SLEEP:
                return context.getString(R.string.main_tab_sleep);
        }
        return null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.time_from:
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                        timeFromCalendar.set(Calendar.HOUR_OF_DAY, hours);
                        timeFromCalendar.set(Calendar.MINUTE, minutes);
                        resetFromButton();
                    }
                }, timeFromCalendar.get(Calendar.HOUR_OF_DAY), timeFromCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity())).show();
                break;
            case R.id.time_to:
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                        timeToCalendar.set(Calendar.HOUR_OF_DAY, hours);
                        timeToCalendar.set(Calendar.MINUTE, minutes);
                        resetToButton();
                    }
                }, timeToCalendar.get(Calendar.HOUR_OF_DAY), timeToCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity())).show();
                break;
            case R.id.activity_end_minus:
                timeToCalendar.add(Calendar.DAY_OF_YEAR, -1);
                resetToButton();
                break;
            case R.id.activity_end_plus:
                timeToCalendar.add(Calendar.DAY_OF_YEAR, 1);
                resetToButton();
                break;
            case R.id.activity_start_minus:
                timeFromCalendar.add(Calendar.DAY_OF_YEAR, -1);
                resetFromButton();
                break;
            case R.id.activity_start_plus:
                timeFromCalendar.add(Calendar.DAY_OF_YEAR, 1);
                resetFromButton();
                break;
            case R.id.save:
                if (type == Contract.Activities.ACTIVITY_SLEEP) {
                    if (timeFromCalendar.getTimeInMillis() > timeToCalendar.getTimeInMillis()) {
                        Toast.makeText(getActivity(), R.string.invalid_time, Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                ContentValues cv = new ContentValues();
                cv.put(Contract.ActivityColumns.ACTIVITY_DESCRIPTION, description.getText().toString());
                cv.put(Contract.ActivityColumns.ACTIVITY_SCORE, score.getSelectedItemPosition());
                cv.put(Contract.ActivityColumns.ACTIVITY_TIME_FROM, timeFromCalendar.getTimeInMillis());
                if (type == Contract.Activities.ACTIVITY_SLEEP)
                    cv.put(Contract.ActivityColumns.ACTIVITY_TIME_TO, timeToCalendar.getTimeInMillis());
                cv.put(Contract.ActivityColumns.ACTIVITY_TYPE, type);
                getActivity().getContentResolver().insert(Contract.Activities.CONTENT_URI, cv);
                dismiss();
                break;
        }
    }

    private void resetFromButton() {
        timeFrom.setText(DateUtils.getDateTimeFormatted(timeFromCalendar.getTimeInMillis()));
    }

    private void resetToButton() {
        timeTo.setText(DateUtils.getDateTimeFormatted(timeToCalendar.getTimeInMillis()));
    }
}
