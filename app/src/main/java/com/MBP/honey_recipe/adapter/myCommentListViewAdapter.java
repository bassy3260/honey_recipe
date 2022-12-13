package com.MBP.honey_recipe.adapter;

/**
 * 내가 쓴 댓글 리사이클러뷰 어댑터
 **/

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.MBP.honey_recipe.Model.Comment;
import com.MBP.honey_recipe.Model.Recipes;

import com.MBP.honey_recipe.R;
import com.MBP.honey_recipe.postActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class myCommentListViewAdapter extends RecyclerView.Adapter<myCommentListViewAdapter.mycommentListViewHolder> {
    // 보여줄 Item 목록을 저장할 List
    private ArrayList<Comment> items;
    private FirebaseFirestore database;
    private Intent intent;
    private ArrayList<Recipes> recipes;

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Adapter 생성자 함수
    public class mycommentListViewHolder extends RecyclerView.ViewHolder {
        public mycommentListViewHolder(@NonNull View view) {
            super(view);
        }
    }

    public myCommentListViewAdapter(FragmentActivity activity, ArrayList<Comment> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public mycommentListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_comment_view, viewGroup, false);
        myCommentListViewAdapter.mycommentListViewHolder mycommentListViewHolder = new myCommentListViewAdapter.mycommentListViewHolder(view);
        recipes=new ArrayList<>();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), postActivity.class);
                int position = mycommentListViewHolder.getAdapterPosition();
                database.collection("recipes")
                        // 카테고리에 따라 게시글 받아오기
                        .whereEqualTo("id", items.get(position).getPostid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.getData().size()>0){
                                            intent.putExtra("id",  document.getData().get("id").toString());
                                            v.getContext().startActivity(intent);
                                        }

                                    }
                                } else {
                                }
                            }
                        });


            }

        });
        return mycommentListViewHolder;
    }

    /* 리스트 뷰 텍스트 설정 */
    @Override
    public void onBindViewHolder(@NonNull mycommentListViewHolder viewHolder, int position) {
        View holder = viewHolder.itemView;
        position = viewHolder.getAdapterPosition();
        TextView comment_name = holder.findViewById(R.id.mycommentNameText);
        TextView post_title = holder.findViewById(R.id.commentTitle);
        ImageView commentImageView = (ImageView) holder.findViewById(R.id.mycommentImageView);
        String userid = items.get(position).getUserid();
        RatingBar ratingBar = (RatingBar) holder.findViewById(R.id.mycommentRatingbar);

        database = FirebaseFirestore.getInstance();
        //FireStore에서 게시글 정보 받아오기
        database.collection("user")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("uid", userid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                comment_name.setText(document.getData().get("name").toString());
                                if (document.getData().get("profile_pic").toString().equals("null")) {
                                    commentImageView.setImageResource(R.drawable.default_profile);

                                } else {
                                    Uri file = Uri.parse(document.getData().get("profile_pic").toString());
                                    Glide.with(holder.getContext()).load(file).centerCrop().override(500).into(commentImageView);
                                }

                            }
                        } else {
                        }
                    }
                });

        database.collection("recipes")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("id", items.get(position).getPostid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               post_title.setText(document.getData().get("title").toString());
                            }
                        } else {
                        }
                    }
                });
        ratingBar.setRating((float) items.get(position).getRating());
        TextView content = holder.findViewById(R.id.mycommentText);
        content.setText(items.get(position).getContent());
        TextView date = holder.findViewById(R.id.mycommentTimeText);
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat format_year = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (format_year.format(now).equals(format_year.format(items.get(position).getCreated()))) {
            if (format_date.format(now).equals(format_date.format(items.get(position).getCreated()))) {
                date.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(items.get(position).getCreated()));
            } else {
                date.setText(new SimpleDateFormat("MM-dd", Locale.getDefault()).format(items.get(position).getCreated()));
            }
        } else {
            date.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(items.get(position).getCreated()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
