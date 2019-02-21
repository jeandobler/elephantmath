package com.dynamic.dobler.elephantmath.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class BaseActivity extends AppCompatActivity {


    private String mGoogleId;

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

        protected void updateUI(GoogleSignInAccount account) {
            if (account == null) {
                Intent splashIntent = new Intent(this, SplashActivity.class);
                startActivity(splashIntent );
            }else {
                mGoogleId = account.getId();
            }
        }

}
