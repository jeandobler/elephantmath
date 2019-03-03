package com.dynamic.dobler.elephantmath.activity.ranking;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.activity.BaseActivity;
import com.dynamic.dobler.elephantmath.database.entity.Ranking;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
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
        setContentView(R.layout.activity_item_list);
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
        mLinearLayoutManager.setStackFromEnd(true);
        mRvRanking.setLayoutManager(mLinearLayoutManager);
        mRvRanking.setHasFixedSize(true);


        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference query = mFirebaseDatabaseReference.child("ranking")
                .orderByChild("email")
                .equalTo("jedobler@gmail.com","email")
                .getRef();


        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                System.out.println("The " + dataSnapshot.getKey() + " score is " + dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            // ...
        });


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

        Log.e("OptionReference", query.toString());
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(options) {

            @Override
            public RankingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new RankingViewHolder(inflater.inflate(R.layout.item_list_content, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final RankingViewHolder viewHolder,
                                            int position,
                                            Ranking ranking) {


                Log.e("Ranking", ranking.toString());

//                Log.e("Error", "asd");
                if (ranking.getPoints() != null) {
                    viewHolder.mText.setText(ranking.getPoints().toString());
                    viewHolder.mContent.setText(ranking.getEmail());
                } else {

                }


                // log a view action on it
//                FirebaseUserActions.getInstance().end(getMessageViewAction(ranking));
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
        TextView mContent;
        TextView mText;


        public RankingViewHolder(View v) {
            super(v);
            mContent = (TextView) itemView.findViewById(R.id.id_text);
            mText = (TextView) itemView.findViewById(R.id.content);
        }
    }

//    private Indexable getMessageIndexable(FriendlyMessage ranking) {
//        PersonBuilder sender = Indexables.personBuilder()
//                .setIsSelf(mUsername.equals(ranking.getName()))
//                .setName(ranking.getName())
//                .setUrl(MESSAGE_URL.concat(ranking.getId() + "/sender"));
//
//        PersonBuilder recipient = Indexables.personBuilder()
//                .setName(mUsername)
//                .setUrl(MESSAGE_URL.concat(ranking.getId() + "/recipient"));
//
//        Indexable messageToIndex = Indexables.messageBuilder()
//                .setName(ranking.getText())
//                .setUrl(MESSAGE_URL.concat(ranking.getId()))
//                .setSender(sender)
//                .setRecipient(recipient)
//                .build();
//
//        return messageToIndex;
//    }


/*
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

        }
    }


*/

}
