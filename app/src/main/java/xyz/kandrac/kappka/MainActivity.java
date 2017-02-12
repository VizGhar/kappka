package xyz.kandrac.kappka;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import xyz.kandrac.kappka.data.Contract;
import xyz.kandrac.kappka.utils.DateUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private Button date;
    private FabSpeedDial fab;
    private ActivitiesFragment fragment;

    private long displayTime = DateUtils.getCurrentDateMilis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FabSpeedDial) findViewById(R.id.fab_btn);
        date = (Button) findViewById(R.id.main_date);
        findViewById(R.id.main_date_increment).setOnClickListener(this);
        findViewById(R.id.main_date_decrement).setOnClickListener(this);
        date.setOnClickListener(this);
        date.setText(DateUtils.getDateFormatted(displayTime));

        fab.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return super.onPrepareMenu(navigationMenu);
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.action_add_eat: {
                        AddFragment.getInstance(Contract.Activities.ACTIVITY_EAT).show(getSupportFragmentManager(), null);
                        break;
                    }
                    case R.id.action_add_sleep: {
                        AddFragment.getInstance(Contract.Activities.ACTIVITY_SLEEP).show(getSupportFragmentManager(), null);
                        break;
                    }
                    case R.id.action_add_poop: {
                        AddFragment.getInstance(Contract.Activities.ACTIVITY_POOP).show(getSupportFragmentManager(), null);
                        break;
                    }
                }
                return false;
            }

            @Override
            public void onMenuClosed() {
                super.onMenuClosed();
            }
        });

        if (savedInstanceState == null) {
            fragment = ActivitiesFragment.getInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_date_increment:
                setDate(DateUtils.incrementDate(displayTime));
                break;
            case R.id.main_date_decrement:
                setDate(DateUtils.decrementDate(displayTime));
                break;
            case R.id.main_date:
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar c = Calendar.getInstance();
                        c.clear();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, day);
                        setDate(c.getTimeInMillis());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    private void setDate(long time) {
        displayTime = time;
        date.setText(DateUtils.getDateFormatted(displayTime));
        fragment.setDateRange(displayTime, DateUtils.incrementDate(displayTime));
    }
}
