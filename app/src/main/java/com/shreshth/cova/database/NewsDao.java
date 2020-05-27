package com.shreshth.cova.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.shreshth.cova.models.News;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert
    void insert(News news);

    @Update
    void update(News news);

    @Delete
    void delete(News news);

    @Query("SELECT * FROM news_table")
    LiveData<List<News>> getllNews();

    @Query("SELECT * FROM news_table WHERE link=:link")
    News getNewsByLink(String link);
}
