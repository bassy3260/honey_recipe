package com.MBP.honey_recipe.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.MBP.honey_recipe.R;
import com.bumptech.glide.Glide;

public class recipeStepView extends LinearLayout {
    private ImageView imageView;
    private EditText editText;

    public recipeStepView(Context context) {
        super(context);
        initView();
    }

    public recipeStepView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    private void initView() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(layoutInflater.inflate(R.layout.recipe_text, this, false));
        addView(layoutInflater.inflate(R.layout.recipe_edittext, this, false));
        addView(layoutInflater.inflate(R.layout.recipe_image, this, false));
        addView(layoutInflater.inflate(R.layout.recipe_delete, this, false));


    }

    public void setImage(String path) {
        Glide.with(this).load(path).override(1000).into(imageView);
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        imageView.setOnClickListener(onClickListener);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        editText.setOnFocusChangeListener(onFocusChangeListener);
    }
}