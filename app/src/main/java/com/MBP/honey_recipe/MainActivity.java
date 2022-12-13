package com.MBP.honey_recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private bestFragment best = new bestFragment();
    private favoriteFragment favorite = new favoriteFragment();
    private mypageFragment mypage = new mypageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, best).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.page_best:
                    transaction.replace(R.id.main_frame, best).commitAllowingStateLoss();
                    break;
                case R.id.page_favorite:
                    transaction.replace(R.id.main_frame, favorite).commitAllowingStateLoss();
                    break;
                case R.id.page_mypage:
                    transaction.replace(R.id.main_frame, mypage).commitAllowingStateLoss();
                    break;

            }

            return true;
        }
    }

    //액션바 타이틀 설정
    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}

