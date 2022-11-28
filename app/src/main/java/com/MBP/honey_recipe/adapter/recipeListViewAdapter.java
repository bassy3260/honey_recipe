package com.MBP.honey_recipe.adapter;

import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.MBP.honey_recipe.Model.recipeStep;
import com.MBP.honey_recipe.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class recipeListViewAdapter extends RecyclerView.Adapter<recipeListViewAdapter.recipeListViewHolder> {

    private ArrayList<recipeStep> items;

    public class recipeListViewHolder extends RecyclerView.ViewHolder{
        public recipeListViewHolder(@NonNull View view) {
            super(view);
        }
    }
    public recipeListViewAdapter(FragmentActivity activity, ArrayList<recipeStep> items) {
        this.items = items;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public recipeListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_step_view, viewGroup, false);
        recipeListViewHolder viewHolder = new recipeListViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull recipeListViewHolder viewHolder, int position) {
        View holder=viewHolder.itemView;
        TextView number=holder.findViewById(R.id.postStepNum);
        TextView step=holder.findViewById(R.id.postStepText);
        ImageView image=holder.findViewById(R.id.postStepImage);

        number.setText(items.get(position).getNum());
        step.setText(items.get(position).getStep());
        Glide.with(viewHolder.itemView.getContext()).load(items.get(position).getStepImage()).centerCrop().into(image);

    }
}
