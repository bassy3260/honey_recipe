package com.MBP.honey_recipe.best;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.MBP.honey_recipe.Model.Recipe;
import com.MBP.honey_recipe.R;
import com.MBP.honey_recipe.adapter.gridViewAdapter;

import java.util.ArrayList;

public class best_rice extends Fragment {
    private GridView gridView;
    private gridViewAdapter adapter;
    private ArrayList<Recipe> recipe;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_best_rice, container, false);
        gridView=(GridView) view.findViewById(R.id.bestricegrid);
        recipe= new ArrayList<Recipe>();
        adapter=new gridViewAdapter(getActivity(),recipe);


        adapter.addItem(new Recipe("쫄볶이",5,"3","3260",R.drawable.icon_best));
        adapter.addItem(new Recipe("라볶이",5,"3","3260",R.drawable.icon_best));
        adapter.addItem(new Recipe("쌀볶이",5,"3","3260",R.drawable.icon_best));
        adapter.addItem(new Recipe("밀볶이",5,"3","3260",R.drawable.icon_best));
        adapter.addItem(new Recipe("약볶이",5,"3","3260",R.drawable.icon_best));
        gridView.setAdapter(adapter);
        return view;
    }
}