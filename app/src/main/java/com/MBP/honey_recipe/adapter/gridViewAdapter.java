package com.MBP.honey_recipe.adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.MBP.honey_recipe.Model.Recipe;
import com.MBP.honey_recipe.R;

import java.util.ArrayList;

public class gridViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Recipe> items = new ArrayList<Recipe>();

    public gridViewAdapter(Context context,ArrayList<Recipe> items ) {
        this.items=items;
    }

    @Override
    public int getCount(){
        return items.size();
    }
    public void addItem(Recipe item) {
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
        Recipe recipe=items.get(position);

        if(view == null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view= inflater.inflate(R.layout.grid_item_view,viewGroup, false);

            TextView recipeName= (TextView) view.findViewById(R.id.recipeName);
            TextView recipeCost= (TextView) view.findViewById(R.id.recipeCost);
            TextView commentCount= (TextView) view.findViewById(R.id.commentCount);
            RatingBar ratingBar= (RatingBar) view.findViewById(R.id.ratingBar);
            ImageView imageView =(ImageView) view.findViewById(R.id.gridImg);

            recipeName.setText(recipe.getRecipeName());
            recipeCost.setText(recipe.getRecipeCost());
            commentCount.setText(recipe.getCommentCount());
            ratingBar.setRating(recipe.getRating());
            imageView.setImageResource(recipe.getResId());

        }else{
            View cview = new View(context);
            cview=(View) view;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,recipe.getRecipeName()+"입니다",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}