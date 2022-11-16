package com.MBP.honey_recipe.Model;

public class Recipe {
    String recipeName;
    int rating;
    String commentCount;
    String recipeCost;
    int resId;

    public Recipe(String recipeName, int rating, String commentCount, String recipeCost, int resId) {
        this.recipeName = recipeName;
        this.rating = rating;
        this.commentCount = commentCount;
        this.recipeCost = recipeCost;
        this.resId = resId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public int getRating() {
        return rating;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public String getRecipeCost() {
        return recipeCost;
    }

    public int getResId() {
        return resId;
    }
}
