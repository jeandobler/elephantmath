package com.dynamic.dobler.elephantmath.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.dynamic.dobler.elephantmath.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SplashActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 901;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);

            updateUI();

        } catch (ApiException e) {
//            updateUI();
        }
    }

    @Override
    protected void updateUI() {
        if (account != null) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        } else {

            if (this.isNetworkConnected()) {
//                Toast.makeText(this, R.string.wrong_account, Toast.LENGTH_SHORT).show();
                signIn();
            } else {
                Toast.makeText(this, R.string.not_connect_splash_exception, Toast.LENGTH_LONG).show();
            }
        }

    }

    private void signIn() {
        if (mGoogleSignInClient != null) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }


}
