package com.MBP.honey_recipe;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.MBP.honey_recipe.Listener.OnPostListener;
import com.MBP.honey_recipe.Model.Comment;
import com.MBP.honey_recipe.Model.Recipes;
import com.MBP.honey_recipe.Model.UserInfo;
import com.MBP.honey_recipe.Model.recipeStep;
import com.MBP.honey_recipe.adapter.commentListViewAdapter;
import com.MBP.honey_recipe.adapter.recipeListViewAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class postActivity extends AppCompatActivity {
    private FirebaseFirestore database;
    private FirebaseUser user;
    private Recipes recipes;
    private String postId;
    private TextView recipeTitle, recipeIngredient, recipeContent,recipeCost,author,rating,commentCount,category;
    private RatingBar ratingBar, commentRatingBar;
    private Button commentButton, favoriteButton;
    private ImageView cookedImage,authorImg;
    private RecyclerView stepRecyclerView, commentRecyclerView;
    private ArrayList<recipeStep> recipeSteps;
    private ArrayList<Comment> commentList;
    private recipeListViewAdapter recipeAdapter;
    private commentListViewAdapter commentAdapter;
    private Date created;
    private ArrayList<String> favoriteList;
    private UserInfo userInfo;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);//액션바를 툴바로 바꿔줌
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        favoriteList=new ArrayList<String>();

        postId = (String) getIntent().getSerializableExtra("id");
        database = FirebaseFirestore.getInstance();
        recipeSteps = new ArrayList<>();
        commentList = new ArrayList<>();
        recipeTitle = (TextView) findViewById(R.id.recipeTitle);
        category=(TextView)findViewById(R.id.categoryTextView);
        recipeCost = (TextView) findViewById(R.id.recipeCostEditText);
        author = (TextView) findViewById(R.id.authorName);
        recipeIngredient = (TextView) findViewById(R.id.recipeIngredient);
        recipeContent = (TextView) findViewById(R.id.writeCommentEditText);
        ratingBar = (RatingBar) findViewById(R.id.recipeRatingBar);
        rating = (TextView) findViewById(R.id.postRatingNum);
        commentCount = (TextView) findViewById(R.id.postCommentCount);
        commentRatingBar = (RatingBar) findViewById(R.id.writeRatingbar);
        commentButton = (Button) findViewById(R.id.commentButton);
        favoriteButton = (Button) findViewById(R.id.favoriteButton);
        cookedImage = (ImageView) findViewById(R.id.cookedImage);
        authorImg = (ImageView) findViewById(R.id.authorImg);
        recipeAdapter = new recipeListViewAdapter(postActivity.this, recipeSteps);
        commentAdapter = new commentListViewAdapter(postActivity.this, commentList);
        stepRecyclerView = (RecyclerView) findViewById(R.id.recipeStepRecyclerView);
        commentRecyclerView = (RecyclerView) findViewById(R.id.commentRecyclerView);
        if(user!=null) {
            userId = user.getUid();
            getUserData();
        }
        CollectionReference collectionReference = database.collection("recipes");
        collectionReference.whereEqualTo("id", postId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                recipes = new Recipes(
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
                                        (Long)document.getData().get("cost"));

                                userId= recipes.getUserId();
                                //글쓴이 정보
                                CollectionReference authorReference = database.collection("user");
                                authorReference.whereEqualTo("uid",recipes.getUserId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                author.setText(document.getData().get("name").toString());
                                                if(document.getData().get("profile_pic")==null){
                                                    authorImg.setImageResource(R.drawable.default_profile);
                                                }else {
                                                    Glide.with(postActivity.this).load(document.getData().get("profile_pic")).centerCrop().into(authorImg);
                                                }
                                            }
                                        } else {
                                            Log.d("로그", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                                getSupportActionBar().setTitle(recipes.getTitle());
                                category.setText(recipes.getCategory());
                                recipeTitle.setText(recipes.getTitle());
                                recipeCost.setText(String.valueOf(recipes.getCost())+"원");
                                recipeIngredient.setText(recipes.getIngredient());
                                recipeContent.setText(recipes.getContent());
                                ratingBar.setRating(recipes.getRating());
                                rating.setText( String.format("%.2f",recipes.getRating()));
                                commentCount.setText("("+recipes.getCommentCount().toString()+")");
                                Glide.with(postActivity.this).load(Uri.parse(recipes.getResultImage())).centerCrop().into(cookedImage);

                                for (int i = 0; i < recipes.getStep().size(); i++) {
                                    recipeStep step = new recipeStep(String.valueOf(i + 1), recipes.getStep().get(i), Uri.parse(String.valueOf(recipes.getStepImage().get(i))));
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




        CollectionReference userReference = database.collection("user");
        userReference.whereEqualTo("uid", userId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                favoriteList= (ArrayList<String>) document.getData().get("favorite");
                                if (favoriteList.contains(postId)) {
                                    favoriteButton.setText("즐겨찾기 추가중");
                                }
                            }
                        } else {
                            Log.d("로그", "Error getting documents: ", task.getException());
                        }
                    }
                });



        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!favoriteList.contains(postId)) {
                    favoriteList = userInfo.getFavorite();
                    favoriteList.add(postId);
                    DocumentReference userRef = database.collection("user").document(user.getUid());

                    userRef.update("favorite", favoriteList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    toast("즐겨찾기 등록 완료");
                                    favoriteButton.setText("즐겨찾기 추가중");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    toast("즐겨찾기 추가 실패");
                                }
                            });

                } else {
                    favoriteList.remove(postId);
                    DocumentReference userRef = database.collection("user").document(user.getUid());
                    userRef.update("favorite", favoriteList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    toast("즐겨찾기 해제 완료");
                                    favoriteButton.setText("즐겨찾기 추가");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    toast("즐겨찾기 해제 실패");
                                }
                            });
                }
            }
        });


        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user==null){
                    toast("로그인이 필요합니다.");
                }else {
                    commentUpload();
                }
            }
        });

        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(postActivity.this));
        commentRecyclerView.setAdapter(commentAdapter);
        commentAdapter.setOnPostListener(onPostListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.post_menu, menu);

        MenuItem postdelete = menu.findItem(R.id.postDeleteMenu);
        if (user!=null&&user.getUid().equals(userId)) {
            postdelete.setVisible(true);
        }
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        commentUpdate();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                // User chose the "Settings" item, show the app settings UI...
                finish();
                break;
            case R.id.postDeleteMenu:
                AlertDialog.Builder builder = new AlertDialog.Builder(postActivity.this);
                builder.setTitle("게시물 삭제");
                builder.setMessage("게시물을 삭제하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {

                        database = FirebaseFirestore.getInstance();
                        database.collection("recipes").document(postId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        toast("게시글을 삭제하였습니다.");
                                        finish();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {

                    }
                });
                builder.create().show();
        }
        return true;
    }
    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(String id) {
            database.collection("comment").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            toast("댓글을 삭제하였습니다.");
                            commentUpdate();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }

    };

    private void commentUpdate() {
        postId = (String) getIntent().getSerializableExtra("id");
        CollectionReference collectionReference = database.collection("comment");
        collectionReference
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("postid", postId)
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
                                Float sum = Float.valueOf(0);
                                for (int i=0; i<commentList.size(); i++ ) {
                                    sum += commentList.get(i).getRating();
                                }
                                database.collection("recipes").document(postId)
                                        .update(
                                                "commentCount", Long.parseLong(String.valueOf(commentList.size())),
                                                "rating", sum/commentList.size()
                                        );
                                ratingBar.setRating( sum/commentList.size());
                                rating.setText( String.format("%.2f", sum/commentList.size()));
                                commentCount.setText("("+String.valueOf(commentList.size())+")");
                            }
                            commentAdapter.notifyDataSetChanged();
                            //리사이클러 뷰 생성

                        } else {
                        }
                    }
                });
    }


    private void commentUpload() {

        final String comment_content = ((EditText) findViewById(R.id.commentEditText)).getText().toString();
        Float rating = Float.parseFloat(String.valueOf(((RatingBar) findViewById(R.id.writeRatingbar)).getRating()));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String comment_uid = user.getUid();
        String post_id = (String) getIntent().getSerializableExtra("id");
        //시간 가져오기
        created = Calendar.getInstance().getTime();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference commentRef = firebaseFirestore.collection("comment").document();
        String comment_id = commentRef.getId();
        if (comment_content.length() > 0) {
            Comment comment = new Comment(comment_id, post_id, comment_uid, comment_content, rating, created);
            commentRef.set(comment)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            DocumentReference recipeRef = database.collection("recipes").document(postId);

                            commentUpdate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toast("댓글 업로드 실패");
                        }
                    });
            Log.e("로그", "url:" + post_id);
        } else {
            toast("댓글을 입력해주세요.");
        }
    }


    private void getUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = database.collection("user");
        collectionReference
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("uid", user.getUid())
                //시간순 정렬
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userInfo = new UserInfo(document.getData().get("name").toString(),
                                        document.getData().get("uid").toString(),
                                        Uri.parse(document.getData().get("profile_pic").toString()),
                                        (ArrayList<String>) document.getData().get("favorite"));
                            }

                        } else {
                        }
                    }
                });

    }

    //토스트메시지 함수
    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}