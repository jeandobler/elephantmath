package com.dynamic.dobler.elephantmath.ui.ranking;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.ui.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailActivity extends AppCompatActivity {

    @BindView(R.id.tb_ranking_detail)
    Toolbar toolbar;

    @BindView(R.id.fab_ranking_detail)
    FloatingActionButton fab;
    private String mPoints;
    private String mId;
    private String TAG = "ItemDetailActivity";
    private String KEY_POINTS = "KEY_POINTS";
    private String KEY_ID = "KEY_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_item_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
//        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        if (savedInstanceState == null) {

            mPoints = getIntent().getStringExtra(ItemDetailFragment.ARG_POINTS);
            mId = getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID);
        } else {

            mPoints = savedInstanceState.getString(KEY_POINTS);
            mId = savedInstanceState.getString(KEY_ID);
        }


        fab.setOnClickListener(view -> {
            Intent minhaIntent = new Intent();
            minhaIntent.setAction(Intent.ACTION_SEND);
            minhaIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.sharre_ranking_title);
            minhaIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_ranking_text)
                    + mPoints);
            minhaIntent.setType("text/plain");
            startActivity(minhaIntent);
        });


        Bundle arguments = new Bundle();
        arguments.putString(ItemDetailFragment.ARG_ITEM_ID,
                mId);
        ItemDetailFragment fragment = new ItemDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Calling the super implementation is very important in this case
        super.onSaveInstanceState(outState);

        outState.putString(KEY_POINTS, mPoints);
        outState.putString(KEY_ID, mId);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
