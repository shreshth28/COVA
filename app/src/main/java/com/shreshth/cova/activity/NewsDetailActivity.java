package com.shreshth.cova.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shreshth.cova.R;
import com.shreshth.cova.models.News;

public class NewsDetailActivity extends AppCompatActivity {

    public String title;
    public String url;
    public String content;
    public String description;
    public String link;
    public String author;
    public String publishedAt;
    public News news;
    public int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Intent intent=getIntent();
        if(intent.hasExtra("Object")) {
            News news =intent.getParcelableExtra("Object");
            title=news.getTitle();
            url=news.getUrlToImage();
            content=news.getContent();
            description=news.getDescription();
            link=news.getLink();
            author=news.getAuthor();
            publishedAt=news.getPublishedAt();
            id=news.getId();
            Toast.makeText(this, id+"", Toast.LENGTH_SHORT).show();
        }
    }
}
