package com.MBP.honey_recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.MBP.honey_recipe.Model.Recipes;
import com.MBP.honey_recipe.Model.recipeStep;
import com.MBP.honey_recipe.View.recipeStepView;
import com.MBP.honey_recipe.adapter.recipeListViewAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;

public class postActivity extends AppCompatActivity {
    private FirebaseFirestore database;
    private FirebaseStorage storage;
    private Recipes recipes;
    private String postId;
    private TextView recipeTitle,recipeIngredient,recipeContent;
    private RatingBar ratingBar,commentRatingBar;
    private Button commentButton;
    private ImageView cookedImage;
    private RecyclerView stepRecyclerView, commentRecyclerView;
    private ArrayList<recipeStep> recipeSteps;
    private recipeListViewAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postId= (String) getIntent().getSerializableExtra("id");
        database=FirebaseFirestore.getInstance();
        recipeSteps=new ArrayList<>();
        recipeTitle= (TextView) findViewById(R.id.recipeTitle);
        recipeIngredient= (TextView) findViewById(R.id.recipeIngredient);
        recipeContent= (TextView) findViewById(R.id.recipeContent);
        ratingBar= (RatingBar) findViewById(R.id.recipeRatingBar);
        commentRatingBar= (RatingBar) findViewById(R.id.commentRatingBar);
        commentButton=(Button) findViewById(R.id.commentButton);
        cookedImage = (ImageView)findViewById(R.id.cookedImage);
        recipeAdapter=new recipeListViewAdapter(postActivity.this,recipeSteps);
        stepRecyclerView=(RecyclerView)findViewById(R.id.recipeStepRecyclerView);
        CollectionReference collectionReference = database.collection("recipes");
        collectionReference.whereEqualTo("id", postId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                recipes=new Recipes(
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
                                        document.getData().get("commentCount").toString());

                                recipeTitle.setText(recipes.getTitle());
                                recipeIngredient.setText(recipes.getIngredient());
                                recipeContent.setText(recipes.getContent());
                                Glide.with(postActivity.this).load(Uri.parse(recipes.getResultImage())).centerCrop().into(cookedImage);

                                for(int i=0;i<recipes.getStep().size();i++){
                                    recipeStep step = new recipeStep(String.valueOf(i+1),recipes.getStep().get(i),Uri.parse(recipes.getStepImage().get(i)));
                                    recipeSteps.add(step);
                                }
                                stepRecyclerView.setLayoutManager(new LinearLayoutManager(postActivity.this));
                                stepRecyclerView.setAdapter(recipeAdapter);
                            }
                        } else {
                            Log.d("로그", "Error getting documents: ", task.getException());
                        }
                    }
                });




    }

    private void postUpdate(){
        postId= (String) getIntent().getSerializableExtra("id");
        database=FirebaseFirestore.getInstance();
        CollectionReference collectionReference = database.collection("recipes");
        collectionReference.whereEqualTo("id", postId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                recipes=new Recipes(
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
                                        document.getData().get("commentCount").toString());
                            }
                        } else {
                            Log.d("로그", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}