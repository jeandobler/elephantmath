package com.dynamic.dobler.elephantmath.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.activity.ranking.ItemDetailActivity;
import com.dynamic.dobler.elephantmath.activity.ranking.ItemDetailFragment;
import com.dynamic.dobler.elephantmath.database.entity.Ranking;
import com.dynamic.dobler.elephantmath.database.entity.RankingItem;
import com.dynamic.dobler.elephantmath.widget.RankingWidget;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChallengeActivity extends BaseActivity {

    private static final String KEY_LIVES = "KEY_LIVES";
    private static final String KEY_PROGESS = "KEY_PROGESS";
    private static final String KEY_LEVEL = "KEY_LEVEL";
    private static final String KEY_POINTS = "KEY_POINTS";
    private static final String KEY_NUMBER1 = "KEY_NUMBER1";
    private static final String KEY_NUMBER2 = "KEY_NUMBER2";
    private static final String KEY_RESULT = "KEY_RESULT";
    private static final String KEY_SAVED = "KEY_SAVED";

    @BindView(R.id.pb_challenge_timer)
    ProgressBar mPbTimer;

    @BindView(R.id.et_challenge_keyboard)
    EditText mEtKeyboard;

    @BindView(R.id.tv_challenge_problem)
    TextView mTvProblem;

    @BindView(R.id.tv_challenge_level)
    TextView mTvLevel;

    @BindView(R.id.iv_challenge_heart1)
    ImageView mHeart1;
    @BindView(R.id.iv_challenge_heart2)
    ImageView mHeart2;
    @BindView(R.id.iv_challenge_heart3)
    ImageView mHeart3;
    Ranking mRanking;
    RankingItem mRankingItem;

    List<RankingItem> mRankingItems;

    private MediaPlayer mp;
    private DatabaseReference mFirebaseDatabaseReference;

    private int progressbarCount = 0;
    private int mLives = 3;
    private int mLevel = 2;
    private int mPoints = 0;
    private int mNumber1;
    private int mNumber2;
    private int mResult;
    private CountDownTimer mCountDownTimer;
    private boolean saved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        ButterKnife.bind(this);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        if (savedInstanceState != null) {
            progressbarCount = savedInstanceState.getInt(KEY_PROGESS);
            mLives = savedInstanceState.getInt(KEY_LIVES);
            mLevel = savedInstanceState.getInt(KEY_LEVEL);
            mPoints = savedInstanceState.getInt(KEY_POINTS);
            mNumber1 = savedInstanceState.getInt(KEY_NUMBER1);
            mNumber2 = savedInstanceState.getInt(KEY_NUMBER2);
            mResult = savedInstanceState.getInt(KEY_RESULT);
            saved = savedInstanceState.getBoolean(KEY_SAVED);

        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        prepareDatabase();
        startLevel();
        observeChallengeEditText();

    }

    @Override
    protected void onPause() {
        super.onPause();
        endGame();
    }

    @Override
    public void onBackPressed() {
        endGame();
        super.onBackPressed();
    }

    void prepareDatabase() {

        Date date = new Date();
        mRanking = new Ranking(this.mGoogleId, date, 0);
        mRankingItems = new ArrayList<RankingItem>();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Calling the super implementation is very important in this case
        super.onSaveInstanceState(outState);


        outState.putInt(KEY_PROGESS, progressbarCount);
        outState.putInt(KEY_LIVES, mLives);
        outState.putInt(KEY_LEVEL, mLevel);
        outState.putInt(KEY_POINTS, mPoints);
        outState.putInt(KEY_NUMBER1, mNumber1);
        outState.putInt(KEY_NUMBER2, mNumber2);
        outState.putInt(KEY_RESULT, mResult);
        outState.putBoolean(KEY_SAVED, saved);
    }

    void observeChallengeEditText() {
        mEtKeyboard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(String.valueOf(mResult))) {
                    mPoints++;
                    if (mPoints % 3 == 0) {
                        mLevel++;
                        if (mLives < 3) {
                            mLives++;
                            checkLives();
                        } else {
                            mPoints++;
                        }

                    }

                    rankingAddItem(true);
                    startLevel();
                }
            }

        });
    }

    private void rankingAddItem(boolean isCorrect) {

        mp = MediaPlayer.create(getApplicationContext(), isCorrect ? R.raw.level_up : R.raw.mktoasty);

        mp.start();
        mp.setOnCompletionListener(MediaPlayer::release);

        RankingItem newItem = new RankingItem(mNumber1, mNumber2, (float) progressbarCount / 10, isCorrect);

        mRankingItems.add(newItem);
    }

    private void startLevel() {

        String levelText = "x" + String.valueOf(mLevel);
        mTvLevel.setText(levelText);
        mTvLevel.setContentDescription(levelText);
        mEtKeyboard.setText("");

        Random rand = new Random();


        mNumber1 = rand.nextInt(mLevel > 10 ? mLevel - 2 : 7) + 2;
        mNumber2 = mLevel;
        mResult = mNumber1 * mNumber2;
        String problemText = String.valueOf(mNumber1) + " x " + String.valueOf(mNumber2) + " =";
        mTvProblem.setText(problemText);
        mTvProblem.setContentDescription(problemText);

        progressbarCount = 0;
        progressbarTimer();

    }


    private void endGame() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mTvProblem.setText(R.string.game_over);
        mTvProblem.setContentDescription(getString(R.string.game_over));
        mTvProblem.setTextSize(R.dimen.title5Text);
        mRanking.setRankingItems(mRankingItems);
        mRanking.setPoints(-mPoints);
        String mGroupId;

        if (!saved) {
            DatabaseReference pushedPostRef = mFirebaseDatabaseReference.child("ranking")
                    .push();
            mGroupId = pushedPostRef.getKey();
            pushedPostRef.setValue(mRanking);
            sendWidgetBroadCast();

            goToRankingList(mGroupId);


            saved = true;
        }


    }

    private void sendWidgetBroadCast() {
        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ranking);
        ComponentName thisWidget = new ComponentName(context, RankingWidget.class);
        remoteViews.setTextViewText(R.id.tv_widget2, String.valueOf(mPoints));
        remoteViews.setContentDescription(R.id.tv_widget2, String.valueOf(mPoints));

        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    private void goToRankingList(String mGroupId) {
        Context context = this;
        Intent intent = new Intent(context, ItemDetailActivity.class);
        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, mGroupId);
        intent.putExtra(ItemDetailFragment.ARG_POINTS, mRanking.getPoints().toString());
        context.startActivity(intent);

    }

    private void checkLives() {

        switch (mLives) {
            case 3:
                mHeart1.setVisibility(View.VISIBLE);
                mHeart2.setVisibility(View.VISIBLE);
                mHeart3.setVisibility(View.VISIBLE);
                startLevel();
                break;
            case 2:
                mHeart1.setVisibility(View.VISIBLE);
                mHeart2.setVisibility(View.VISIBLE);
                mHeart3.setVisibility(View.INVISIBLE);
                startLevel();
                break;
            case 1:
                mHeart1.setVisibility(View.VISIBLE);
                mHeart2.setVisibility(View.INVISIBLE);
                mHeart3.setVisibility(View.INVISIBLE);
                startLevel();
                break;
            case 0:
                endGame();
                break;
        }


    }


    private void progressbarTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mPbTimer.setProgress(progressbarCount);
        mCountDownTimer = new CountDownTimer(10000, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
                progressbarCount++;
                mPbTimer.setProgress(progressbarCount);

            }

            @Override
            public void onFinish() {
                //Do what you want
                rankingAddItem(false);
                progressbarCount++;
                mPbTimer.setProgress(100);
                mLives -= 1;
                checkLives();
            }
        };
        mCountDownTimer.start();
    }
}
