package com.example.oireporttool.Database;

import java.util.Date;

public class Post {
    public int post_Id;
    public String post_title;
    public String post_details;
    public String record_date;
    public String userId;
    public String post_imageUrl;
    public String post_audioUrl;
    public String post_category;
    public String post_tag;
    public String post_project;


    public Post(int post_Id, String post_title, String post_details, String record_date, String userId, String post_imageUrl, String post_audioUrl,String post_category,String post_tag,String post_project) {
        this.post_Id = post_Id;
        this.post_title = post_title;
        this.post_details = post_details;
        this.record_date = record_date;
        this.userId = userId;
        this.post_imageUrl = post_imageUrl;
        this.post_audioUrl = post_audioUrl;
        this.post_category = post_category;
        this.post_tag= post_tag;
        this.post_project = post_project;
    }

    public Post() {

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

    public String getRecord_date() {
        return record_date.toString();
    }

//    public void setRecord_date(Date record_date) {
//        this.record_date = record_date;
//    }

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

    public String getPost_category() {
        return post_category;
    }

    public void setPost_category(String post_category) {
        this.post_category = post_category;
    }

    public String getPost_tag() {
        return post_tag;
    }

    public void setPost_tag(String post_tag) {
        this.post_tag = post_tag;
    }

    public String getPost_project() {
        return post_project;
    }

    public void setPost_project(String post_project) {
        this.post_project = post_project;
    }
}