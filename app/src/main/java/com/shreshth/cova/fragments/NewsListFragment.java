package com.shreshth.cova.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shreshth.cova.R;
import com.shreshth.cova.activity.NewsDetailActivity;
import com.shreshth.cova.adapter.NewsAdapter;
import com.shreshth.cova.database.NewsViewModel;
import com.shreshth.cova.models.News;
import com.shreshth.cova.network.NEWSParser;
import com.shreshth.cova.network.NetworkHelper;
import com.shreshth.cova.widget.CovaAppWidgetProvider;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class NewsListFragment extends Fragment implements NewsAdapter.NewsItemClickListener {


    private NewsViewModel newsViewModel;
    NewsAdapter newsAdapter;
    RecyclerView newsListRecyclerView;
    List<News>newsList;
    static public List<News>fromDatabase;
    String selected="latest";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rv=inflater.inflate(R.layout.fragment_news_list,container,false);

        Toolbar toolbar =rv.findViewById(R.id.news_list_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("News Feed");
        newsViewModel= ViewModelProviders.of(getActivity()).get(NewsViewModel.class);
        newsViewModel.getAllNews().observe(getActivity(), new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                fromDatabase=news;
                if(selected.equals("favourite"))
                {
                    showFavourites();
                }
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
                ComponentName thisWidget = new ComponentName(getActivity(), CovaAppWidgetProvider.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredient_widget_list_view);
            }
        });
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
        Intent intent=new Intent(getActivity().getBaseContext(),NewsDetailActivity.class);
        if(!selected.equals("favourite")) {
            intent.putExtra("Object", newsList.get(index));
        }
        else{
            intent.putExtra("Object", fromDatabase.get(index));
        }
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.sort,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.favourites_option:
                showFavourites();
                break;
            case R.id.latest_option:
                showLatest();
        }
        return super.onOptionsItemSelected(item);
    }


    private void showLatest() {
        selected="latest";
        newsAdapter.setData(newsList);
        newsAdapter.notifyDataSetChanged();
    }

    private void showFavourites() {
        selected="favourite";
        newsAdapter.setData(fromDatabase);
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        newsAdapter.notifyDataSetChanged();
    }

}
