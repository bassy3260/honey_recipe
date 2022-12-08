package com.MBP.honey_recipe.Model;

import android.net.Uri;

import java.util.ArrayList;

public class UserInfo {

    public String name;
    public String uid;
    public Uri profile_pic;
    public ArrayList<String> favorite;

    public UserInfo(String name, String uid, Uri profile_pic, ArrayList<String> favorite) {
        this.name = name;
        this.uid = uid;
        this.profile_pic = profile_pic;
        this.favorite = favorite;
    }

    public UserInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Uri getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(Uri profile_pic) {
        this.profile_pic = profile_pic;
    }

    public ArrayList<String> getFavorite() {
        return favorite;
    }

    public void setFavorite(ArrayList<String> favorite) {
        this.favorite = favorite;
    }
}