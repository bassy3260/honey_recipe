package com.MBP.honey_recipe.Categories;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.MBP.honey_recipe.Model.Recipe;
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

public class category_rice extends Fragment {
    private GridView gridView;
    private gridViewAdapter adapter;
    private FirebaseFirestore database;
    private ArrayList<Recipes> recipes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_best_rice, container, false);
        gridView=(GridView) view.findViewById(R.id.bestricegrid);
        recipes= new ArrayList<Recipes>();
        adapter=new gridViewAdapter(getActivity(),recipes);
        database = FirebaseFirestore.getInstance();
        postUpdate();
        gridView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        postUpdate();
    }
    private void postUpdate(){

        CollectionReference collectionReference = database.collection("recipes");
        collectionReference.whereEqualTo("category", "밥류").orderBy("created", Query.Direction.DESCENDING).get()
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
                                        (ArrayList<String>) document.getData().get("stepImage"),
                                        new Date(document.getDate("created").getTime()),
                                        document.getData().get("rating").toString(),
                                        document.getData().get("commentCount").toString()));
                            }
                            adapter.notifyDataSetChanged(); //데이터 변경(삭제, 수정 시)
                        } else {
                            Log.d("로그", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}