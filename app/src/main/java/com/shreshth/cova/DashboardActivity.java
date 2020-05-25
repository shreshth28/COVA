package com.shreshth.cova;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shreshth.cova.network.NetworkHelper;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    List<AuthUI.IdpConfig> providers;
    Button community_chat_btn;
    public static final int RC_SIGN_IN=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // Choose authentication providers
        community_chat_btn=findViewById(R.id.community_chat_btn);
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        if(FirebaseAuth.getInstance().getCurrentUser()==null) {
            showSignInOptions();
        }
        else {
            Toolbar toolbar = findViewById(R.id.dashboard_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("DASHBOARD");
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryText));
            community_chat_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent communityChatIntent = new Intent(DashboardActivity.this, CommunityChatActivity.class);
                    startActivity(communityChatIntent);
                }
            });
           makeSearchQuery();
        }

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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, "Error Signing In", Toast.LENGTH_SHORT).show();
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
                e.printStackTrace();
                Toast.makeText(DashboardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
