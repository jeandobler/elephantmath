package com.dynamic.dobler.elephantmath.activity.ranking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.activity.BaseActivity;
import com.dynamic.dobler.elephantmath.database.converter.DateConverter;
import com.dynamic.dobler.elephantmath.database.entity.Ranking;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListActivity extends BaseActivity {

    @BindView(R.id.tbRankingItems)
    Toolbar toolbar;

    @BindView(R.id.rv_ranking_list)
    RecyclerView mRvRanking;


    private boolean mTwoPane;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Ranking, RankingViewHolder>
            mFirebaseAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(getTitle());

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

        setupRecyclerView();
    }

    private void setupRecyclerView() {

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvRanking.setLayoutManager(mLinearLayoutManager);
        mRvRanking.setHasFixedSize(true);


        Query query = mFirebaseDatabaseReference
                .child("ranking")
                .orderByChild("points");


        SnapshotParser<Ranking> parser = dataSnapshot -> {
            Ranking ranking = dataSnapshot.getValue(Ranking.class);
            if (ranking != null) {
                ranking.setId(dataSnapshot.getKey());
            }
            return ranking;
        };

        FirebaseRecyclerOptions<Ranking> options =
                new FirebaseRecyclerOptions.Builder<Ranking>()
                        .setQuery(query, parser)
                        .build();


        mFirebaseAdapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(options) {

            private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Ranking item = (Ranking) view.getTag();
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.getId());
                        arguments.putString(ItemDetailFragment.ARG_POINTS, item.getPoints().toString());
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.getId());
                        intent.putExtra(ItemDetailFragment.ARG_POINTS, item.getPoints().toString());


                        context.startActivity(intent);
                    }
                }
            };

            @Override
            public RankingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new RankingViewHolder(inflater.inflate(R.layout.list_ranking, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final RankingViewHolder viewHolder, int position,
                                            Ranking ranking) {


                if (ranking.getPoints() != null) {

                    String pontsValue = String.valueOf(-1 * ranking.getPoints());
                    viewHolder.rTvName.setText(ranking.getEmail());
                    viewHolder.rTvPoints.setText(pontsValue);
                    viewHolder.rTvDate.setText(DateConverter.toNormalDate(ranking.getCreatedAt()));
                    viewHolder.itemView.setTag(ranking);
                    viewHolder.itemView.setOnClickListener(mOnClickListener);

                } else {
//                    empty
                }
            }
        };

        mRvRanking.setAdapter(mFirebaseAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAdapter.stopListening();
    }

    public static class RankingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ranking_date)
        TextView rTvDate;

        @BindView(R.id.tv_ranking_points)
        TextView rTvPoints;

        @BindView(R.id.tv_ranking_name)
        TextView rTvName;


        public RankingViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

}
