package com.shreshth.cova.network;

import android.util.Log;
import android.widget.Toast;

import com.shreshth.cova.DashboardActivity;
import com.shreshth.cova.models.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {

    public static ArrayList<Country> parseData(String data)
    {
        try {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray =jsonObject.getJSONArray("Countries");
            int length=jsonArray.length();
            ArrayList<Country> arrayList=new ArrayList<>();
            for(int x=0;x<length;x++)
            {
                JSONObject arrayJSONObject=jsonArray.getJSONObject(x);
                String country=arrayJSONObject.getString("Country");
                String countryCode=arrayJSONObject.getString("CountryCode");
                int newConfirmed=arrayJSONObject.getInt("NewConfirmed");
                int totalConfirmed=arrayJSONObject.getInt("TotalConfirmed");
                int newDeaths=arrayJSONObject.getInt("NewDeaths");
                int totalDeaths=arrayJSONObject.getInt("TotalDeaths");
                int newRecovered=arrayJSONObject.getInt("NewRecovered");
                int totalRecovered=arrayJSONObject.getInt("TotalRecovered");
                Country myCountry=new Country(country,countryCode,newConfirmed,totalConfirmed,newDeaths,totalDeaths,newRecovered,totalRecovered);
                arrayList.add(myCountry);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONPARSER",e.getMessage());
        }

        return null;
    }
}
