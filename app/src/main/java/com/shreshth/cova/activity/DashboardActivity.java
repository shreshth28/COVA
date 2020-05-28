package com.shreshth.cova.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.shreshth.cova.R;
import com.shreshth.cova.models.Country;
import com.shreshth.cova.network.JSONParser;
import com.shreshth.cova.network.NetworkHelper;
import com.shreshth.cova.notification.ReminderUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    ArrayList<Country> myList;
    List<AuthUI.IdpConfig> providers;
    Button community_chat_btn;
    BarChart chart;
    MaterialSpinner spinner;
    String choice="United States Of America";
    Button newsFeedBtn;
    Toolbar toolbar;
    Animation slide_down;
    Animation slide_up;
    CardView cardView1;
    CardView cardView3;
    AdView mAdView;

    public static final int RC_SIGN_IN=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_dashboard);
        slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        community_chat_btn=findViewById(R.id.community_chat_btn);
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        if(FirebaseAuth.getInstance().getCurrentUser()==null) {
            showSignInOptions();
        }
        else {
            buildApp();
            chart.setNoDataText(getString(R.string.select_country_mssg));
            if(savedInstanceState!=null) {
                choice = savedInstanceState.getString("choice");
            }

        }


    }

    private void buildApp() {

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        cardView1=findViewById(R.id.cardView1);
        cardView3=findViewById(R.id.cardView3);
        cardView1.startAnimation(slide_up);
        cardView3.startAnimation(slide_up);

        toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.dashboard));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryText));
        community_chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent communityChatIntent = new Intent(DashboardActivity.this, CommunityChatActivity.class);
                startActivity(communityChatIntent);
            }
        });
        chart=findViewById(R.id.chart);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(DashboardActivity.this, R.string.pinch_zoom_mssg, Toast.LENGTH_SHORT).show();
                choice=item;
                setUpBarGraph();
            }
        });
        newsFeedBtn=findViewById(R.id.news_feed_btn);
        newsFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newsFeedIntent=new Intent(DashboardActivity.this,NewsActivity.class);
                startActivity(newsFeedIntent);
            }
        });
        ReminderUtilities.scheduleChargingReminder(this);
        makeSearchQuery();
    }

    private void makeSearchQuery() {
        URL url= NetworkHelper.buildNetworkUrl();
        GetCovidData getCovidData=new GetCovidData();
        getCovidData.execute(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                buildApp();
                chart.setNoDataText(getString(R.string.select_country_mssg));
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, R.string.error_signin_mssg, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showSignInOptions() {

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.logo)
                        .setTheme(R.style.MyTheme)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.logout,menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.logout_option)
        {
            FirebaseAuth.getInstance().signOut();
            showSignInOptions();
        }
        return super.onOptionsItemSelected(item);
    }

    class GetCovidData extends AsyncTask<URL,Void,String>
    {

        @Override
        protected String doInBackground(URL... urls) {
            URL url=urls[0];
            String response = null;
            try {
                response=NetworkHelper.getResponseFromHttpUrl(url);
            } catch (IOException e) {
            }
            return response;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            myList=new ArrayList<>();
            if(s==null)
            {
                makeSearchQuery();
//                Toast.makeText(DashboardActivity.this, "Request Failed, Making Another Request", Toast.LENGTH_SHORT).show();
                return;
            }
            myList= JSONParser.parseData(s);
            setUpBarGraph();
        }
    }

    private void setUpBarGraph() {
        List<String> countryNames=new ArrayList<>();
        for(int x=0;x<myList.size();x++)
        {
            countryNames.add(myList.get(x).getCountry());
        }
        spinner.setItems(countryNames);
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int x=0;x<myList.size();x++)
        {
            Country object=myList.get(x);
            if(object.getCountry().equals(choice))
            {

                try {
                    entries.add(new BarEntry(0,object.getNewConfirmed()));
                    entries.add(new BarEntry(1,object.getTotalConfirmed()));
                    entries.add(new BarEntry(2,object.getTotalRecovered()));
                    entries.add(new BarEntry(3,object.getTotalDeaths()));
                    entries.add(new BarEntry(4,object.getNewRecovered()));
                    entries.add(new BarEntry(5,object.getNewDeaths()));
                    final ArrayList<String>fields = new ArrayList<String>();
                    fields.add(getString(R.string.new_confirmed));
                    fields.add(getString(R.string.total_confirmed));
                    fields.add(getString(R.string.total_recovered));
                    fields.add(getString(R.string.total_deaths));
                    fields.add(getString(R.string.new_recovered));
                    fields.add(getString(R.string.new_deaths));
                    BarDataSet bardataset = new BarDataSet(entries, getString(R.string.bar_dataset));
                    chart.animateY(5000);
                    BarData data = new BarData(bardataset);
                    data.setBarWidth(1f);
                    bardataset.setColors(ColorTemplate.JOYFUL_COLORS);
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setGranularityEnabled(true);
                    xAxis.setGranularity(1.0f);
                    xAxis.setLabelCount(4);
                    xAxis.mLabelWidth=1;
                    xAxis.mEntryCount=6;
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(fields));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    chart.setData(data);

                }
                catch (Exception e)
                {
                    Toast.makeText(DashboardActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("choice",choice);
    }
}
