package com.dynamic.dobler.elephantmath.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dynamic.dobler.elephantmath.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class SplashActivity : AppCompatActivity() {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var account: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_splash)

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        signIn()
    }

    private val isNetworkConnected: Boolean
        private get() {
            val cm =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        }

    public override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            account = completedTask.getResult(ApiException::class.java)
            updateUI()
        } catch (e: ApiException) {
            e.printStackTrace()
            if (isNetworkConnected) {
                signIn()
            } else {
                Toast.makeText(this, R.string.not_connect_splash_exception, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun updateUI() {
        if (account != null) {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        } else {
            if (isNetworkConnected) {
                signIn()
            } else {
                Toast.makeText(this, R.string.not_connect_splash_exception, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun signIn() {
        if (mGoogleSignInClient != null) {
            val signInIntent = mGoogleSignInClient!!.signInIntent
            startActivityForResult(signInIntent,
                RC_SIGN_IN
            )
        }
    }

    companion object {
        private const val RC_SIGN_IN = 901
    }
}