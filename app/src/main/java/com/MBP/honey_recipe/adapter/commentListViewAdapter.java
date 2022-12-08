package com.MBP.honey_recipe.adapter;


/** 게시글 리사이클러뷰 어댑터 **/
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.MBP.honey_recipe.Model.Comment;
import com.MBP.honey_recipe.R;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class commentListViewAdapter extends RecyclerView.Adapter<commentListViewAdapter.commentListViewHolder> {
    // 보여줄 Item 목록을 저장할 List
    private ArrayList<Comment> items=new ArrayList<>();
    private FirebaseFirestore database;
    private FirebaseUser user;
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class commentListViewHolder extends RecyclerView.ViewHolder{
        public commentListViewHolder(@NonNull View view) {
            super(view);
        }
    }

    public commentListViewAdapter(FragmentActivity activity, ArrayList<Comment> items) {
        this.items = items;
    }
    @NonNull
    @Override
    public commentListViewHolder  onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_view, viewGroup, false);
        commentListViewHolder commentListViewHolder = new commentListViewHolder (view);
        return   commentListViewHolder;
    }

    /* 리스트 뷰 텍스트 설정 */
    @Override
    public void onBindViewHolder(@NonNull commentListViewHolder viewHolder, int position) {
        View holder = viewHolder.itemView;
        position=viewHolder.getAdapterPosition();
        TextView comment_name = holder.findViewById(R.id.commentNameText);
        ImageView commentImageView = (ImageView) holder.findViewById(R.id.commentImageView);
        String userid = items.get(position).getUserid();
        RatingBar ratingBar=(RatingBar) holder.findViewById(R.id.commentRatingbar);

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
                                    String url = document.getData().get("profile_pic").toString();
                                    Uri file = Uri.parse(url);
                                    Glide.with(holder.getContext()).load(file).centerCrop().override(500).into(commentImageView);
                                }

                            }
                        } else {
                        }
                    }
                });
        ratingBar.setRating(items.get(position).getRating());
        TextView content = holder.findViewById(R.id.commentText);
        content.setText(items.get(position).getContent());
        TextView date = holder.findViewById(R.id.commentTimeText);
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
        user = FirebaseAuth.getInstance().getCurrentUser();
//        ImageButton commentDeleteButton = (ImageButton) holder.findViewById(R.id.commentDeleteButton);
//        if (userid.equals(user.getUid())) {
//            commentDeleteButton.setVisibility(holder.VISIBLE);
//        }
        String id = (String) items.get(position).getId();
//        commentDeleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(holder.getContext());
//                builder.setTitle("댓글 삭제");
//                builder.setMessage("댓글을 삭제하시겠습니까?");
//                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                    public void onClick(
//                            DialogInterface dialog, int id) {
//                        if (uid.equals(user.getUid())) {
//                            onPostListener.onDelete(id);
//                            notifyItemChanged(position); //x 표시가 밀리는 현상 고침.
//                        }
//                        else{
//                            Toast.makeText(v.getContext(), "자신의 댓글만 삭제할 수 있습니다.", Toast.LENGTH_SHORT );
//                        }
//                    }
//                });
//                builder.setNegativeButton("취소",  new DialogInterface.OnClickListener() {
//                    public void onClick(
//                            DialogInterface dialog, int id) {
//
//                    }
//                });
//                builder.create().show();
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}