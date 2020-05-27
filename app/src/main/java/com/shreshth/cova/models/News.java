package com.shreshth.cova.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news_table")
public class News implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String urlToImage;
    private String content;
    private String author;
    private String publishedAt;
    private String link;

    public News(String title, String description, String urlToImage, String content, String author, String publishedAt, String link) {
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.content = content;
        this.author = author;
        this.publishedAt = publishedAt;
        this.link = link;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected News(Parcel in) {
        title = in.readString();
        description = in.readString();
        urlToImage = in.readString();
        content = in.readString();
        author = in.readString();
        publishedAt = in.readString();
        link = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

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

    public String getAuthor() {
        return author;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getLink() {
        return link;
    }
    public int getId() {
        return id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(urlToImage);
        dest.writeString(content);
        dest.writeString(author);
        dest.writeString(publishedAt);
        dest.writeString(link);
    }
}
