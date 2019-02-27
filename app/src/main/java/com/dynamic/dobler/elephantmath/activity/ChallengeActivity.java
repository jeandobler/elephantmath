package com.dynamic.dobler.elephantmath.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

import com.dynamic.dobler.elephantmath.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChallengeActivity extends AppCompatActivity {

    @BindView(R.id.pb_challenge_timer)
    ProgressBar mPbChallengeTimer;
    int total = 0;
    CountDownTimer cdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        ButterKnife.bind(this);

        progressbarTimer();


    }

    private void progressbarTimer() {
        mPbChallengeTimer.setProgress(total);
        int oneMin = 1 * 60 * 1000; // 1 minute in milli seconds

        /** CountDownTimer starts with 1 minutes and every onTick is 1 second */
        cdt = new CountDownTimer(oneMin, 1000) {

            public void onTick(long millisUntilFinished) {

                total = (int) ((millisUntilFinished / 60) * 100);
                mPbChallengeTimer.setProgress(total);
            }

            public void onFinish() {
                // DO something when 1 minute is up
            }
        }.start();
    }
}
