package com.MBP.honey_recipe.Categories;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.MBP.honey_recipe.Model.Recipes;
import com.MBP.honey_recipe.R;
import com.MBP.honey_recipe.adapter.gridViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class category_etc extends Fragment {
    private GridView gridView;
    private gridViewAdapter adapter;
    private FirebaseFirestore database;
    private ArrayList<Recipes> recipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best_etc, container, false);
        gridView = (GridView) view.findViewById(R.id.bestetcgrid);
        recipes = new ArrayList<Recipes>();

        database = FirebaseFirestore.getInstance();
        Log.e("출력", String.valueOf(recipes));
        AppCompatButton best= view.findViewById(R.id.etcBest);
        AppCompatButton newB = view.findViewById(R.id.etcNew);
        newB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newB.setBackgroundColor(Color.parseColor("#ACACAC"));
                best.setBackgroundColor(Color.parseColor("#D8D8D8"));
                newPostUpdate();
            }
        });
        best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                best.setBackgroundColor(Color.parseColor("#ACACAC"));
                newB.setBackgroundColor(Color.parseColor("#D8D8D8"));
                bestPostUpdate();
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        bestPostUpdate();
    }

    private void newPostUpdate() {

        CollectionReference collectionReference = database.collection("recipes");
        collectionReference.whereEqualTo("category", "기타").orderBy("created", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            recipes.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                recipes.add(new Recipes(
                                        document.getData().get("id").toString(),
                                        document.getData().get("userId").toString(),
                                        document.getData().get("title").toString(),
                                        document.getData().get("category").toString(),
                                        document.getData().get("content").toString(),
                                        document.getData().get("ingredient").toString(),
                                        document.getData().get("resultImage").toString(),
                                        (ArrayList<String>) document.getData().get("step"),
                                        (ArrayList<Uri>) document.getData().get("stepImage"),
                                        new Date(document.getDate("created").getTime()),
                                        Float.parseFloat(document.getData().get("rating").toString()),
                                        (Long) document.getData().get("commentCount"),
                                        (Long) document.getData().get("cost")));
                            }

                            adapter = new gridViewAdapter(getActivity(), recipes);
                            gridView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("로그", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void bestPostUpdate() {
        CollectionReference collectionReference = database.collection("recipes");
        collectionReference.whereEqualTo("category", "기타").orderBy("rating", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            recipes.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                recipes.add(new Recipes(
                                        document.getData().get("id").toString(),
                                        document.getData().get("userId").toString(),
                                        document.getData().get("title").toString(),
                                        document.getData().get("category").toString(),
                                        document.getData().get("content").toString(),
                                        document.getData().get("ingredient").toString(),
                                        document.getData().get("resultImage").toString(),
                                        (ArrayList<String>) document.getData().get("step"),
                                        (ArrayList<Uri>) document.getData().get("stepImage"),
                                        new Date(document.getDate("created").getTime()),
                                        Float.parseFloat(document.getData().get("rating").toString()),
                                        (Long) document.getData().get("commentCount"),
                                        (Long) document.getData().get("cost")));
                            }

                            adapter = new gridViewAdapter(getActivity(), recipes);
                            gridView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("로그", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}