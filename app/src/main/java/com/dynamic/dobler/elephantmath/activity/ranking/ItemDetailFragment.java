package com.dynamic.dobler.elephantmath.activity.ranking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.adapter.RankingDetailsAdapter;
import com.dynamic.dobler.elephantmath.database.entity.Ranking;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";


    private Ranking mItem;
    private DatabaseReference mFirebaseDatabaseReference;
    private FragmentActivity activity;
    private CollapsingToolbarLayout appBarLayout;
    private DatabaseReference ref;
    private RecyclerView rankingDetailRecycleView;
    private LinearLayoutManager layoutManager;
    private RankingDetailsAdapter mRankingDetailsAdapter;


    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this.getActivity();
        appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.ct_ranking_detail);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        if (getArguments().containsKey(ARG_ITEM_ID)) {

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            ref = database.getReference("ranking/" + getArguments().getString(ARG_ITEM_ID));


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ranking_item_detail, container, false);


        rankingDetailRecycleView = (RecyclerView) rootView.findViewById(R.id.rv_ranking_detail_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rankingDetailRecycleView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        rankingDetailRecycleView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        ;

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mItem = dataSnapshot.getValue(Ranking.class);
//                ((TextView) rootView.findViewById(R.id.tv_ranking_detail)).setText(mItem.getEmail());

                mRankingDetailsAdapter = new RankingDetailsAdapter(mItem.getRankingItems());
                rankingDetailRecycleView.setAdapter(mRankingDetailsAdapter);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mItem.getEmail());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                 
            }
        });

        return rootView;
    }
}
