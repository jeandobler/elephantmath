package com.dynamic.dobler.elephantmath.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.activity.ranking.ItemListActivity;

import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.cv_main_ranking)
    CardView mCvRanking;


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

    }

    protected void openRanking() {
        Intent rankingIntent = new Intent(this, ItemListActivity.class);
        startActivity(rankingIntent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

}
