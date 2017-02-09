package xyz.kandrac.kappka;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // set viewpager
        mViewPager = (ViewPager) findViewById(R.id.content_pager);
        mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));

        // set tabs
        final TabLayout tabs = (TabLayout) findViewById(R.id.main_tab_holder);
        tabs.setupWithViewPager(mViewPager);
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
            switch (position){
                case 0: return getString(R.string.main_tab_eat);
                case 1: return getString(R.string.main_tab_sleep);
                case 2: return getString(R.string.main_tab_poop);
                default: return null;
            }
        }
    }
}
