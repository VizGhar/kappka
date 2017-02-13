package xyz.kandrac.kappka;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import xyz.kandrac.kappka.data.Contract;
import xyz.kandrac.kappka.data.firebase.FirebaseFeedback;
import xyz.kandrac.kappka.data.firebase.References;
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
                        AddFragment.getInstance(Contract.Activities.ACTIVITY_EAT, displayTime).show(getSupportFragmentManager(), null);
                        break;
                    }
                    case R.id.action_add_sleep: {
                        AddFragment.getInstance(Contract.Activities.ACTIVITY_SLEEP, displayTime).show(getSupportFragmentManager(), null);
                        break;
                    }
                    case R.id.action_add_poop: {
                        AddFragment.getInstance(Contract.Activities.ACTIVITY_POOP, displayTime).show(getSupportFragmentManager(), null);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select_clear:
                fragment.clearType();
                return true;
            case R.id.action_select_eat:
                fragment.setType(Contract.Activities.ACTIVITY_EAT);
                return true;
            case R.id.action_select_sleep:
                fragment.setType(Contract.Activities.ACTIVITY_SLEEP);
                return true;
            case R.id.action_select_poop:
                fragment.setType(Contract.Activities.ACTIVITY_POOP);
                return true;
            case R.id.action_feedback:

                final EditText edittext = new EditText(this);

                new AlertDialog.Builder(this)
                        .setMessage(R.string.feedback_message)
                        .setTitle(R.string.feedback_title)
                        .setView(edittext)
                        .setPositiveButton(R.string.action_send, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseFeedback feedback = new FirebaseFeedback();
                                feedback.comment = edittext.getText().toString();
                                FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child(References.FEEDBACK_REFERENCE)
                                        .push()
                                        .setValue(feedback);
                                Toast.makeText(MainActivity.this, R.string.feedback_thanks, Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton(R.string.action_cancel, null)
                        .show();

                return true;
        }
        return super.onOptionsItemSelected(item);
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
