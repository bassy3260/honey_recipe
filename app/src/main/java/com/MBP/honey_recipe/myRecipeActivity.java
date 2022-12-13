package com.MBP.honey_recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.MBP.honey_recipe.Model.Recipes;
import com.MBP.honey_recipe.adapter.gridViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class myRecipeActivity extends AppCompatActivity {
    private GridView gridView;
    private gridViewAdapter adapter;
    private FirebaseFirestore database;
    private FirebaseUser user;
    private ArrayList<Recipes> recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);
        Toolbar toolbar = findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);//액션바를 툴바로 바꿔줌
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("내가 작성한 레시피");
        gridView=(GridView) findViewById(R.id.myRecipeGridView);
        database = FirebaseFirestore.getInstance();
        recipes= new ArrayList<Recipes>();
        postUpdate();
        adapter=new gridViewAdapter(myRecipeActivity.this,recipes);

        gridView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                // User chose the "Settings" item, show the app settings UI...
                finish();
                break;
        }
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        postUpdate();
    }
    private void postUpdate(){
        user= FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = database.collection("recipes");
        collectionReference.whereEqualTo("userId",user.getUid()).orderBy("created", Query.Direction.DESCENDING).get()
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
                                        Float.parseFloat( document.getData().get("rating").toString()),
                                        (Long)document.getData().get("commentCount"),
                                        (Long)document.getData().get("cost")));
                            }
                            adapter.notifyDataSetChanged(); //데이터 변경(삭제, 수정 시)
                        } else {
                            Log.d("로그", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}