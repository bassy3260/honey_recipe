package com.MBP.honey_recipe.Model;

import java.util.Date;

public class Comment {
    String id;
    String postid;
    String userid;
    String content;
    Float rating;
    Date created;

    public Comment(String id, String postid, String userid, String content,  Float rating, Date created) {
        this.id = id;
        this.postid = postid;
        this.userid = userid;
        this.content = content;
        this.rating = rating;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public  Float getRating() {
        return rating;
    }

    public void setRating( Float rating) {
        this.rating = rating;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
