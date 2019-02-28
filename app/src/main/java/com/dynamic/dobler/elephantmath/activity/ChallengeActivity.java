package com.dynamic.dobler.elephantmath.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.database.entity.Ranking;
import com.dynamic.dobler.elephantmath.database.entity.RankingItem;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChallengeActivity extends BaseActivity {

    @BindView(R.id.pb_challenge_timer)
    ProgressBar mPbChallengeTimer;

    int progressbarCount = 0;
    int lives = 3;
    Ranking mRanking;
    RankingItem mRankingItem;
    private CountDownTimer mCountDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        Date date = new Date();
        mRanking = new Ranking(null, this.mGoogleId, date, 0);
        ButterKnife.bind(this);

        progressbarTimer();


    }

    private void progressbarTimer() {
        mPbChallengeTimer.setProgress(progressbarCount);
        mCountDownTimer = new CountDownTimer(10000, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("Log_tag", "Tick of Progress " + progressbarCount + " " + millisUntilFinished);
                progressbarCount++;
                mPbChallengeTimer.setProgress(progressbarCount);

            }

            @Override
            public void onFinish() {
                //Do what you want
                progressbarCount++;
                mPbChallengeTimer.setProgress(100);
            }
        };
        mCountDownTimer.start();
    }
}
