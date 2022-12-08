package com.MBP.honey_recipe.adapter;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.MBP.honey_recipe.Model.Recipes;
import com.MBP.honey_recipe.R;
import com.MBP.honey_recipe.postActivity;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Date;

public class favoriteListViewAdapter extends RecyclerView.Adapter<favoriteListViewAdapter.favoriteListViewHolder> {
    // 보여줄 Item 목록을 저장할 List
    private ArrayList<String> items;
    private Intent intent;

    private FirebaseUser user;
    private FirebaseFirestore database;

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Adapter 생성자 함수
    public class favoriteListViewHolder extends RecyclerView.ViewHolder {
        public favoriteListViewHolder(@NonNull View view) {
            super(view);
        }
    }

    public favoriteListViewAdapter(FragmentActivity activity, ArrayList<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public favoriteListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favorite_view, viewGroup, false);
        favoriteListViewHolder ViewHolder = new favoriteListViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), postActivity.class);
                int position = ViewHolder.getAdapterPosition();

                intent.putExtra("id", items.get(position));
                v.getContext().startActivity(intent);
            }
        });
        return ViewHolder;
    }

    /* 리스트 뷰 텍스트 설정 */
    @Override
    public void onBindViewHolder(@NonNull favoriteListViewHolder viewHolder, int position) {
        View holder = viewHolder.itemView;
        user = FirebaseAuth.getInstance().getCurrentUser();
        TextView favTitle = (TextView) holder.findViewById(R.id.favoriteTitleEditText);
        ImageView favImage = (ImageView) holder.findViewById(R.id.favoriteImageView);
        ImageButton favBtn = (ImageButton) holder.findViewById(R.id.favoriteBtn);
        ImageButton deleteFavBtn = (ImageButton) holder.findViewById(R.id.deletefavoriteBtn);
        database = FirebaseFirestore.getInstance();
        String postId = items.get(position);
        database.collection("recipes").whereEqualTo("id", postId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                favTitle.setText(document.getData().get("title").toString());
                                Glide.with(holder.getContext()).load(Uri.parse(document.getData().get("resultImage").toString())).centerCrop().into(favImage);

                            }
                        } else {
                            Log.d("로그", "Error getting documents: ", task.getException());
                        }

                    }
                });


        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.add(postId);
                DocumentReference userRef = database.collection("user").document(user.getUid());
                userRef.update("favorite", items)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                favBtn.setVisibility(View.GONE);
                                deleteFavBtn.setVisibility(View.VISIBLE);
                                Toast.makeText(view.getContext(), "즐겨찾기 추가 완료", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(), "즐겨찾기 추가 실패", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        deleteFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.remove(postId);
                DocumentReference userRef = database.collection("user").document(user.getUid());
                userRef.update("favorite", items)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                deleteFavBtn.setVisibility(View.GONE);
                                favBtn.setVisibility(View.VISIBLE);
                                Toast.makeText(view.getContext(), "즐겨찾기 삭제 완료", Toast.LENGTH_SHORT).show();
                           }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(), "즐겨찾기 삭제 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
