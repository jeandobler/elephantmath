package com.dynamic.dobler.elephantmath.activity.ranking;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.activity.BaseActivity;
import com.dynamic.dobler.elephantmath.activity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

public class ItemDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_ranking_detail);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_ranking_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            fab.setOnClickListener(view -> {
                Intent minhaIntent = new Intent();
                minhaIntent.setAction(Intent.ACTION_SEND);
                minhaIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.sharre_ranking_title);
                minhaIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_ranking_text)
                        + getIntent().getStringExtra(ItemDetailFragment.ARG_POINTS));
                minhaIntent.setType("text/plain");
                startActivity(minhaIntent);
            });


            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();


        }
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
