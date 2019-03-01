package com.dynamic.dobler.elephantmath.activity;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {


    protected String mGoogleId;

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    protected void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            Intent splashIntent = new Intent(this, SplashActivity.class);
            startActivity(splashIntent);
        } else {
            mGoogleId = account.getId();
        }
    }

}
