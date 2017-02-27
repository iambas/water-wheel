package com.darker.test.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.darker.test.R;
import com.darker.test.fragment.ScreenSlideFragment;

public class KnowlageActivity extends AppCompatActivity{
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowlage);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("ประวัติ"));
        tabLayout.addTab(tabLayout.newTab().setText("ส่วนประกอบ"));
        tabLayout.addTab(tabLayout.newTab().setText("ประโยชน์"));
        tabLayout.addTab(tabLayout.newTab().setText("สารคดี"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new SimplePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}

class SimplePagerAdapter extends FragmentStatePagerAdapter {

    public static final int NUM_PAGES = 4;

    public SimplePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new ScreenSlideFragment().newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}