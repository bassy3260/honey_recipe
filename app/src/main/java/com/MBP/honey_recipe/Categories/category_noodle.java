package com.MBP.honey_recipe.Categories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.MBP.honey_recipe.Model.Recipes;
import com.MBP.honey_recipe.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class category_noodle extends Fragment {
    private FirebaseFirestore database;
    private ArrayList<Recipes> recipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_best_noodle, container, false);
    }
}