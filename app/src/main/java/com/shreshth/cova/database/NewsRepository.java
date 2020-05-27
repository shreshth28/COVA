package com.shreshth.cova.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.shreshth.cova.models.News;
import com.shreshth.cova.network.AppExecutors;

import java.util.List;

public class NewsRepository {

    private NewsDao newsDao;
    private LiveData<List<News>> allNews;

    public NewsRepository(Application application)
    {
        NewsDatabase database=NewsDatabase.getInstance(application);
        newsDao=database.newsDao();
        allNews=newsDao.getllNews();
    }

    public void insert(final News news)
    {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                newsDao.insert(news);
            }
        });
    }


    public void delete(final News news)
    {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                newsDao.delete(news);
            }
        });

    }

    public LiveData<List<News>> getAllNews(){
        return allNews;
    }
}

