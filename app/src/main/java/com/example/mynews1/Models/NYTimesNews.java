package com.example.mynews1.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NYTimesNews {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("published_date")
    @Expose
    private String publishedDate;
    @SerializedName("url")
    @Expose
    private String imageURL;

    private List<NYTimesNews> nyTimesNewsList;

    public List<NYTimesNews> getNyTimesNewsList() {
        return nyTimesNewsList;
    }

    public void setNyTimesNewsList(List<NYTimesNews> nyTimesNewsList) {
        this.nyTimesNewsList = nyTimesNewsList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }



    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }
}