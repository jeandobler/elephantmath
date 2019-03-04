package com.dynamic.dobler.elephantmath.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.database.entity.RankingItem;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RankingDetailsAdapter extends RecyclerView.Adapter<RankingDetailsAdapter.RankingDetailsHolder> {
    private List<RankingItem> mDataset;

    public RankingDetailsAdapter(List<RankingItem> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RankingDetailsAdapter.RankingDetailsHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_ranking_details, parent, false);

        RankingDetailsHolder vh = new RankingDetailsHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RankingDetailsHolder holder, int position) {

        int result = mDataset.get(position).getNumber1() * mDataset.get(position).getNumber2();
        holder.mTvN1.setText(String.valueOf(mDataset.get(position).getNumber1()));
        holder.mTvN2.setText(String.valueOf(mDataset.get(position).getNumber2()));
        holder.mTvResult.setText(String.valueOf(result));

        if (mDataset.get(position).isCorrect()) {
            holder.mIvCorrect.setImageResource(R.drawable.correct);
        } else {
            holder.mIvCorrect.setImageResource(R.drawable.incorrect);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return mDataset != null ? mDataset.size() : 0;
    }

    public static class RankingDetailsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ranking_details_result)
        public TextView mTvResult;

        @BindView(R.id.tv_ranking_details_correct)
        public ImageView mIvCorrect;

        @BindView(R.id.tv_ranking_details_n1)
        public TextView mTvN1;

        @BindView(R.id.tv_ranking_details_n2)
        public TextView mTvN2;


        public RankingDetailsHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}