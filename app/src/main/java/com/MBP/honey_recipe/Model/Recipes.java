package com.MBP.honey_recipe.Model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;

public class Recipes {
    String id;
    String userId;
    String title;
    String category;
    String content;
    String ingredient;
    String resultImage;
    ArrayList<String> step;
    ArrayList<String> stepImage;
    Date created;
    String rating;
    String commentCount;

    public Recipes(String id, String userId, String title, String category, String content, String ingredient, String resultImage, ArrayList<String> step, ArrayList<String> stepImage, Date created, String rating, String commentCount) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.category = category;
        this.content = content;
        this.ingredient = ingredient;
        this.resultImage = resultImage;
        this.step = step;
        this.stepImage = stepImage;
        this.created = created;
        this.rating = rating;
        this.commentCount = commentCount;
    }

    public Recipes() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getResultImage() {
        return resultImage;
    }

    public void setResultImage(String resultImage) {
        this.resultImage = resultImage;
    }

    public ArrayList<String> getStep() {
        return step;
    }

    public void setStep(ArrayList<String> step) {
        this.step = step;
    }

    public ArrayList<String> getStepImage() {
        return stepImage;
    }

    public void setStepImage(ArrayList<String> stepImage) {
        this.stepImage = stepImage;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }
}