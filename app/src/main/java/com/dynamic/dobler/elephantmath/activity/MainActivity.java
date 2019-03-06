package com.dynamic.dobler.elephantmath.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.activity.ranking.ItemListActivity;

import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.cv_main_ranking)
    CardView mCvRanking;

    @BindView(R.id.cv_main_challenge)
    CardView mCvChallenge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCvRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRanking();
            }
        });

        mCvChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChallenge();
            }
        });

    }

    protected void openRanking() {
        Intent rankingIntent = new Intent(this, ItemListActivity.class);
        startActivity(rankingIntent);
    }

    protected void openChallenge() {
        Intent challengeIntent = new Intent(this, ChallengeActivity.class);
        startActivity(challengeIntent);
    }


}
