package com.shreshth.cova.network;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkHelper {

    static final String BASE_URL="https://api.covid19api.com";
    static final String BASE_URL_NEWS="https://newsapi.org/v2/top-headlines";

    public static URL buildNetworkUrl()
    {
        Uri builtUri=Uri.parse(BASE_URL).buildUpon()
                .appendPath("summary")
                .build();

        URL url=null;
        try {
            url=new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static URL buildNewsNetworkUrl()
    {
        Uri builtUri=Uri.parse(BASE_URL_NEWS).buildUpon()
                .appendQueryParameter("q","corona")
                .appendQueryParameter("apiKey","f60c05a24752454ba405eafbb6ac730b")
                .build();

        URL url=null;
        try {
            url=new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
