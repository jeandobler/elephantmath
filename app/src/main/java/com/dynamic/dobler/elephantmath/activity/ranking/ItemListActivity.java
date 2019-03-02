package com.dynamic.dobler.elephantmath.activity.ranking;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.activity.BaseActivity;
import com.dynamic.dobler.elephantmath.activity.ranking.dummy.DummyContent;
import com.dynamic.dobler.elephantmath.database.entity.Ranking;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.builders.PersonBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListActivity extends BaseActivity {

    private boolean mTwoPane;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Ranking, RankingViewHolder>
            mFirebaseAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_ranking_list)
    RecyclerView mRvRanking;
    private LinearLayoutManager mLinearLayoutManager;


    public static class RankingViewHolder extends RecyclerView.ViewHolder {
        TextView mContent;
        TextView mText;


        public RankingViewHolder(View v) {
            super(v);
            mContent = (TextView) itemView.findViewById(R.id.id_text);
            mText = (TextView) itemView.findViewById(R.id.content);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

        assert mRvRanking != null;
        setupRecyclerView(  mRvRanking);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        SnapshotParser<Ranking> parser = new SnapshotParser<Ranking>() {
            @Override
            public Ranking parseSnapshot(DataSnapshot dataSnapshot) {
                Ranking ranking = dataSnapshot.getValue(Ranking.class);
                if (ranking != null) {
                    ranking.setId(dataSnapshot.getKey());
                }
                return ranking;
            }
        };

        DatabaseReference messagesRef = mFirebaseDatabaseReference.child("ranking");

        FirebaseRecyclerOptions<Ranking> options =
                new FirebaseRecyclerOptions.Builder<Ranking>()
                        .setQuery(messagesRef, parser)
                        .build();

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

                Log.e("Error", "asd");
                if (ranking.getPoints() != null) {
                    viewHolder.mContent.setText(ranking.getPoints());
                    viewHolder.mContent.setText(ranking.getEmail());
                } else {
                }


                // log a view action on it
//                FirebaseUserActions.getInstance().end(getMessageViewAction(ranking));
            }
        };




        mRvRanking.setLayoutManager(mLinearLayoutManager);
        mRvRanking.setAdapter(mFirebaseAdapter);

    }

//    private Indexable getMessageIndexable(FriendlyMessage friendlyMessage) {
//        PersonBuilder sender = Indexables.personBuilder()
//                .setIsSelf(mUsername.equals(friendlyMessage.getName()))
//                .setName(friendlyMessage.getName())
//                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId() + "/sender"));
//
//        PersonBuilder recipient = Indexables.personBuilder()
//                .setName(mUsername)
//                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId() + "/recipient"));
//
//        Indexable messageToIndex = Indexables.messageBuilder()
//                .setName(friendlyMessage.getText())
//                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId()))
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
