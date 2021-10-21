package com.learnandroid.notes;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.learnandroid.notes.adapter.PagerAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    TabLayout tabLayout;
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Finding the necessary views
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);
        viewPager2 = findViewById(R.id.viewPager);


        setSupportActionBar(toolbar);

        //Adding tabs
        TabLayout.Tab tab1 = tabLayout.newTab().setIcon(R.drawable.ic_note);
        TabLayout.Tab tab2 = tabLayout.newTab().setIcon(R.drawable.ic_check);
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);

        setStartTab(tab1,tab2);

        //All for the fragments
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager2.setCurrentItem(tab.getPosition());
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.tabSelectedIconColor);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.tabUnSelectedIconColor);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



    }

    private void setStartTab(TabLayout.Tab tab1,TabLayout.Tab tab2) {
        int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.tabSelectedIconColor);
        Objects.requireNonNull(tab1.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        int tabIconColor1 = ContextCompat.getColor(getApplicationContext(), R.color.tabUnSelectedIconColor);
        tab2.getIcon().setColorFilter(tabIconColor1,PorterDuff.Mode.SRC_IN);

    }

}