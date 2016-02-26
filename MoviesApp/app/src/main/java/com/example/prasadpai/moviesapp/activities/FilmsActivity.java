package com.example.prasadpai.moviesapp.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.example.prasadpai.moviesapp.R;
import com.example.prasadpai.moviesapp.fragments.MoviesFragment;


public class FilmsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ( !isNetworkAvailable() ){
         Toast.makeText(getBaseContext(), R.string.no_netwrok_avialbale,Toast.LENGTH_SHORT).show();
        }
        else {

            setContentView(R.layout.activity_main);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new MoviesFragment())
                        .commit();
            }

        }
    }


    private boolean isNetworkAvailable() {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            boolean isAvailable = false;

            if (networkInfo != null && networkInfo.isConnected()) {
                isAvailable = true;
            }

            return isAvailable;
        }


    }

