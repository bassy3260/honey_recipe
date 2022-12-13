package com.MBP.honey_recipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.MBP.honey_recipe.adapter.bestViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class bestFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private View view;
    private ViewPager mViewPager;
    bestViewPagerAdapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (ViewGroup)inflater.inflate(R.layout.fragment_best, container, false);
        setHasOptionsMenu(true);

        mViewPager = (ViewPager) view.findViewById(R.id.bestcategoryViewpager);
        adapter= new bestViewPagerAdapter(getChildFragmentManager(),5);
        mViewPager.setAdapter(adapter);
        mViewPager.setSaveEnabled(false);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.recipe_home_menu,menu);

    }
    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ((MainActivity) activity).setActionBarTitle("레시피");
        }
    }
    //메뉴 버튼 설정 함수
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //마이페이지 이동 버튼
        mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        switch (item.getItemId()){
            case R.id.menu_write:
                if(user==null){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), writeActivity.class);
                    startActivity(intent);
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

}