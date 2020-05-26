package com.shreshth.cova.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.shreshth.cova.R;
import com.shreshth.cova.network.JSONParser;
import com.shreshth.cova.network.NetworkHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        GetNewsData getNewsData=new GetNewsData();
        URL url=NetworkHelper.buildNewsNetworkUrl();
        getNewsData.execute(url);

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
                Toast.makeText(NewsActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                Toast.makeText(NewsActivity.this,s, Toast.LENGTH_LONG).show();
                Log.d(NewsActivity.class.getSimpleName(),s);
            }
        }
    }
}
