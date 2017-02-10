package xyz.kandrac.kappka;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import xyz.kandrac.kappka.utils.DateUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private Button date;

    private long displayTime = DateUtils.getCurrentDateMilis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.fab).setOnClickListener(this);
        date = (Button) findViewById(R.id.main_date);
        findViewById(R.id.main_date_increment).setOnClickListener(this);
        findViewById(R.id.main_date_decrement).setOnClickListener(this);
        date.setOnClickListener(this);
        date.setText(DateUtils.getDateFormatted(displayTime));

        // set viewpager
        mViewPager = (ViewPager) findViewById(R.id.content_pager);
        mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));

        // set tabs
        final TabLayout tabs = (TabLayout) findViewById(R.id.main_tab_holder);
        tabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                AddFragment.getInstance(mViewPager.getCurrentItem()).show(getSupportFragmentManager(), null);
                break;
            case R.id.main_date_increment:
                displayTime = DateUtils.incrementDate(displayTime);
                date.setText(DateUtils.getDateFormatted(displayTime));
                break;
            case R.id.main_date_decrement:
                displayTime = DateUtils.decrementDate(displayTime);
                date.setText(DateUtils.getDateFormatted(displayTime));
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
                        displayTime = c.getTimeInMillis();
                        date.setText(DateUtils.getDateFormatted(displayTime));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter {

        private final Fragment[] items = new Fragment[]{
                EatFragment.getInstance(),
                SleepFragment.getInstance(),
                PoopFragment.getInstance()
        };

        private TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return items[position];
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.main_tab_eat);
                case 1:
                    return getString(R.string.main_tab_sleep);
                case 2:
                    return getString(R.string.main_tab_poop);
                default:
                    return null;
            }
        }
    }
}
