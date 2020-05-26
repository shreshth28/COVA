package com.shreshth.cova.network;

import com.shreshth.cova.models.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NEWSParser {


    public static List<News> parseNewsJson(String data)
    {
        List<News> newsList=new ArrayList<>();
        try {

            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("articles");
            for(int x=0;x<jsonArray.length();x++)
            {
                JSONObject singleObject=jsonArray.getJSONObject(x);
                String title=singleObject.getString("title");
                String description=singleObject.getString("description");
                String urlToImage=singleObject.getString("urlToImage");
                String content=singleObject.getString("content");
                News news=new News(title,description,urlToImage,content);
                newsList.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsList;

    }
}
