package com.MBP.honey_recipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.MBP.honey_recipe.Model.Comment;
import com.MBP.honey_recipe.Model.Recipes;
import com.MBP.honey_recipe.R;
import com.MBP.honey_recipe.postActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.IntSummaryStatistics;

public class gridViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Recipes> items = new ArrayList<Recipes>();
    private FirebaseFirestore database;
    private FirebaseStorage storage;
    public gridViewAdapter(Context context, ArrayList<Recipes> items ) {
        this.items=items;
    }
    ArrayList<Float> rating;
    @Override
    public int getCount(){
        return items.size();
    }
    public void addItem(Recipes item) {
        items.add(item);
    }
    @Override
    public Object getItem(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){

        context = viewGroup.getContext();
        Recipes recipe=items.get(position);
        FirebaseFirestore database;

        if(view == null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view= inflater.inflate(R.layout.grid_item_view,viewGroup, false);

            TextView recipeName= (TextView) view.findViewById(R.id.gridRecipeName);
            TextView recipeCost= (TextView) view.findViewById(R.id.gridRecipeCost);
            TextView commentCount= (TextView) view.findViewById(R.id.gridRatingScore);
            RatingBar ratingBar= (RatingBar) view.findViewById(R.id.gridRatingBar);
            ImageView imageView =(ImageView) view.findViewById(R.id.gridImg);
            ratingBar.setRating(recipe.getRating());
            commentCount.setText("("+recipe.getCommentCount().toString()+")");
            Glide.with(view.getContext()).load(Uri.parse(recipe.getResultImage())).centerCrop().into(imageView);

            recipeName.setText(recipe.getTitle());
            recipeCost.setText(recipe.getCategory());




        }else{
            View cview = new View(context);
            cview=(View) view;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,recipe.getTitle()+"입니다",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), postActivity.class);
                intent.putExtra("id",items.get(position).getId());
                view.getContext().startActivity(intent);


            }
        });
        return view;
    }
}