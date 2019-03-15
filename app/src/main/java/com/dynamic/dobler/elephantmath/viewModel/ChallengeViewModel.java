package com.dynamic.dobler.elephantmath.viewModel;

import android.media.MediaPlayer;
import android.os.CountDownTimer;

import com.dynamic.dobler.elephantmath.database.entity.Ranking;
import com.dynamic.dobler.elephantmath.database.entity.RankingItem;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import androidx.lifecycle.ViewModel;

public class ChallengeViewModel extends ViewModel {


    public Ranking mRanking;
    public RankingItem mRankingItem;

    public List<RankingItem> mRankingItems;

    public MediaPlayer mp;
    public DatabaseReference mFirebaseDatabaseReference;

    public int timerMs = 10000;
    public int progressbarCount = 0;
    public int mLives = 3;
    public int mLevel = 2;
    public int mPoints = 0;
    public int mNumber1;
    public int mNumber2;
    public int mResult;
    public CountDownTimer mCountDownTimer;
    public boolean saved = false;
    public boolean mStarTed = false;



}
