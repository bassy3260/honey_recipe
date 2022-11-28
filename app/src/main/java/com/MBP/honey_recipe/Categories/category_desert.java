package com.MBP.honey_recipe.Categories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MBP.honey_recipe.R;

public class category_desert extends Fragment {

    ViewPager viewPager;

    public void Fragment_Total(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_best_desert, container, false);
    }
}