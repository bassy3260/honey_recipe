package com.MBP.honey_recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.MBP.honey_recipe.Model.Comment;
import com.MBP.honey_recipe.adapter.myCommentListViewAdapter;
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

public class myCommentActivity extends AppCompatActivity {
    private myCommentListViewAdapter adapter;
    private String postId;
    private RecyclerView mycommentRecylerView;
    private FirebaseUser user;
    private ArrayList<Comment> commentList;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);

        database=FirebaseFirestore.getInstance();
        commentList=new ArrayList<>();


    }

    @Override
    public void onResume() {
        super.onResume();
        commentUpdate();
    }
    private void commentUpdate() {
        user= FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = database.collection("comment");
        collectionReference
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("userid", user.getUid())
                //시간순 정렬
                .orderBy("created", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            commentList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                commentList.add(new Comment(
                                        document.getData().get("id").toString(),
                                        document.getData().get("postid").toString(),
                                        document.getData().get("userid").toString(),
                                        document.getData().get("content").toString(),
                                        Float.parseFloat(document.getData().get("rating").toString()),
                                        new Date(document.getDate("created").getTime())));
                            }
                            mycommentRecylerView= (RecyclerView) findViewById(R.id.mycommentRecyclerView);
                            adapter= new myCommentListViewAdapter(myCommentActivity.this,commentList);
                            mycommentRecylerView.setLayoutManager(new LinearLayoutManager(myCommentActivity.this));
                            mycommentRecylerView.setHasFixedSize(true);
                            mycommentRecylerView.setAdapter(adapter);
                        } else {
                        }
                    }
                });
    }
}