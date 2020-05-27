package com.shreshth.cova.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shreshth.cova.R;
import com.shreshth.cova.activity.NewsDetailActivity;
import com.shreshth.cova.adapter.NewsAdapter;
import com.shreshth.cova.models.News;
import com.shreshth.cova.network.NEWSParser;
import com.shreshth.cova.network.NetworkHelper;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class NewsListFragment extends Fragment implements NewsAdapter.NewsItemClickListener {

    NewsAdapter newsAdapter;
    RecyclerView newsListRecyclerView;
    List<News>newsList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rv=inflater.inflate(R.layout.fragment_news_list,container,false);
        Toolbar toolbar =rv.findViewById(R.id.news_list_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("News Feed");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
        newsListRecyclerView =rv.findViewById(R.id.news_list_recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        newsAdapter=new NewsAdapter(this);
        newsListRecyclerView.setLayoutManager(linearLayoutManager);
        newsListRecyclerView.setAdapter(newsAdapter);
        GetNewsData getNewsData=new GetNewsData();
        URL url=NetworkHelper.buildNewsNetworkUrl();
        getNewsData.execute(url);
        return rv;
    }

    @Override
    public void onNewsClickListener(int index) {
//        Toast.makeText(getContext(), index+" ", Toast.LENGTH_SHORT).show();
//        Intent detailNewsIntent=new Intent(getContext(), NewsDetailActivity.class);
//        startActivity(detailNewsIntent);
        Intent intent=new Intent(getActivity().getBaseContext(),NewsDetailActivity.class);
        intent.putExtra("Object",newsList.get(index));
        getActivity().startActivity(intent);
    }

    class GetNewsData extends AsyncTask<URL,Void,String>
    {

        @Override
        protected String doInBackground(URL... urls) {
            URL url=urls[0];
            String response = null;
            try {
                response= NetworkHelper.getResponseFromHttpUrl(url);
            } catch (IOException e) {
            }
            return response;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s==null)
            {
                return;
            }
            else{
                newsList=NEWSParser.parseNewsJson(s);
                newsAdapter.setData(newsList);
                newsAdapter.notifyDataSetChanged();
            }
        }
    }
}
