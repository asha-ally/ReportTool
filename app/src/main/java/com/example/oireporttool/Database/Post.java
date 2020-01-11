package com.example.oireporttool.Database;

import java.util.Date;

public class Post {
    private int post_Id;
    private String post_title;
    private String post_details;
    private Date record_date;
    private String userId;
    private String post_imageUrl;
    private String post_audioUrl;

    public Post(int post_Id, String post_title, String post_details, Date record_date, String userId, String post_imageUrl, String post_audioUrl) {
        this.post_Id = post_Id;
        this.post_title = post_title;
        this.post_details = post_details;
        this.record_date = record_date;
        this.userId = userId;
        this.post_imageUrl = post_imageUrl;
        this.post_audioUrl = post_audioUrl;
    }

    public int getPost_Id() {
        return post_Id;
    }

    public void setPost_Id(int post_Id) {
        this.post_Id = post_Id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_details() {
        return post_details;
    }

    public void setPost_details(String post_details) {
        this.post_details = post_details;
    }

    public Date getRecord_date() {
        return record_date;
    }

    public void setRecord_date(Date record_date) {
        this.record_date = record_date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPost_imageUrl() {
        return post_imageUrl;
    }

    public void setPost_imageUrl(String post_imageUrl) {
        this.post_imageUrl = post_imageUrl;
    }

    public String getPost_audioUrl() {
        return post_audioUrl;
    }

    public void setPost_audioUrl(String post_audioUrl) {
        this.post_audioUrl = post_audioUrl;
    }
}