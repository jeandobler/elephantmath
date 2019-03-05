package com.dynamic.dobler.elephantmath.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {


    public String mGoogleId = null;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    protected void updateUI() {
        if (mGoogleId == null) {
            account = GoogleSignIn.getLastSignedInAccount(this);
            if (account == null) {
                Intent splashIntent = new Intent(this, SplashActivity.class);
                startActivity(splashIntent);
            } else {
                this.mGoogleId = account.getDisplayName();

            }
        }
    }

    protected boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
