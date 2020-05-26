package com.shreshth.cova.models;

public class News {


    private String title;
    private String description;
    private String urlToImage;
    private String content;


    public News(String title, String description, String urlToImage, String content) {
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getContent() {
        return content;
    }
}
