package com.shreshth.cova.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shreshth.cova.R;
import com.shreshth.cova.activity.NewsActivity;
import com.shreshth.cova.network.NEWSParser;
import com.shreshth.cova.network.NetworkHelper;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class NewsListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rv=inflater.inflate(R.layout.fragment_news_list,container,false);
        GetNewsData getNewsData=new GetNewsData();
        URL url=NetworkHelper.buildNewsNetworkUrl();
        getNewsData.execute(url);
        return rv;
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
//                Toast.makeText(NewsActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                List newsList=NEWSParser.parseNewsJson(s);
                Toast.makeText(getActivity(),s, Toast.LENGTH_LONG).show();
                Log.d(NewsActivity.class.getSimpleName(),newsList.toString());
            }
        }
    }
}
