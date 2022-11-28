package com.MBP.honey_recipe.Model;

public class UserInfo {

    public String name;
    public String uid;
    public String profile_pic;

    public UserInfo(String name,  String uid, String profile_pic) {
        this.name = name;

        this.uid = uid;
        this.profile_pic = profile_pic;
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

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}