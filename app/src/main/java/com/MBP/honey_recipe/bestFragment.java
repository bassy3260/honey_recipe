package com.MBP.honey_recipe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MBP.honey_recipe.adapter.bestViewPagerAdapter;


public class bestFragment extends Fragment {

    private View view;
    private ViewPager mViewPager;
    bestViewPagerAdapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (ViewGroup)inflater.inflate(R.layout.fragment_best, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.bestcategoryViewpager);
        adapter= new bestViewPagerAdapter(getChildFragmentManager(),5);
        mViewPager.setAdapter(adapter);
        mViewPager.setSaveEnabled(false);

        return view;
    }
}