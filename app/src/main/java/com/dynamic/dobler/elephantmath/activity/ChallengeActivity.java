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
import com.dynamic.dobler.elephantmath.viewModel.ChallengeViewModel;
import com.dynamic.dobler.elephantmath.widget.RankingWidget;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChallengeActivity extends BaseActivity {

    public String TAG = "challengeActivity";
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
    private ChallengeViewModel mModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        ButterKnife.bind(this);

        mModel = ViewModelProviders.of(this).get(ChallengeViewModel.class);
        mModel.mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mModel.mStarTed) {
            prepareDatabase();
            startLevel();
            mModel.mStarTed = true;
        } else {
            continueLevel();
        }
        observeChallengeEditText();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mModel.mCountDownTimer != null) {
            mModel.mCountDownTimer.cancel();
        }

        if (mModel.mp != null) {
            mModel.mp.release();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        endGame();
        super.onBackPressed();
    }

    void prepareDatabase() {
        Date date = new Date();
        mModel.mRanking = new Ranking(this.mGoogleId, date, 0);
        mModel.mRankingItems = new ArrayList<RankingItem>();
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
                if (s.toString().equals(String.valueOf(mModel.mResult))) {
                    mModel.mPoints++;
                    if (mModel.mPoints % 3 == 0) {
                        mModel.mLevel++;
                        if (mModel.mLives < 3) {
                            mModel.mLives++;
                            checkLives();
                        } else {
                            mModel.mPoints++;
                        }

                    }

                    rankingAddItem(true);
                    startLevel();
                }
            }

        });
    }

    private void rankingAddItem(boolean isCorrect) {

        mModel.mp = MediaPlayer.create(getApplicationContext(), isCorrect ? R.raw.level_up : R.raw.mktoasty);

        mModel.mp.start();
        mModel.mp.setOnCompletionListener(MediaPlayer::release);

        RankingItem newItem = new RankingItem(mModel.mNumber1, mModel.mNumber2, (float) mModel.progressbarCount / 10, isCorrect);

        mModel.mRankingItems.add(newItem);
    }

    private void startLevel() {


        mEtKeyboard.setText("");

        Random rand = new Random();


        mModel.mNumber1 = rand.nextInt(mModel.mLevel > 10 ? mModel.mLevel - 2 : 7) + 2;
        continueLevel();

        mModel.progressbarCount = 0;
        progressbarTimer();

    }

    private void continueLevel() {
        String levelText = "x" + String.valueOf(mModel.mLevel);
        mTvLevel.setText(levelText);
        mTvLevel.setContentDescription(levelText);

        mModel.mNumber2 = mModel.mLevel;
        mModel.mResult = mModel.mNumber1 * mModel.mNumber2;
        String problemText = String.valueOf(mModel.mNumber1) + " x " + String.valueOf(mModel.mNumber2) + " =";
        mTvProblem.setText(problemText);
        mTvProblem.setContentDescription(problemText);
        progressbarTimer();
    }


    private void endGame() {
        if (mModel.mCountDownTimer != null) {
            mModel.mCountDownTimer.cancel();
        }
        mTvProblem.setText(R.string.game_over);
        mTvProblem.setContentDescription(getString(R.string.game_over));
        mTvProblem.setTextSize(R.dimen.title5Text);
        mModel.mRanking.setRankingItems(mModel.mRankingItems);
        mModel.mRanking.setPoints(-mModel.mPoints);
        String mGroupId;

        if (!mModel.saved) {
            DatabaseReference pushedPostRef = mModel.mFirebaseDatabaseReference.child("ranking")
                    .push();
            mGroupId = pushedPostRef.getKey();
            pushedPostRef.setValue(mModel.mRanking);
            sendWidgetBroadCast();

            goToRankingList(mGroupId);


            mModel.saved = true;
        }


    }


    private void sendWidgetBroadCast() {
        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ranking);
        ComponentName thisWidget = new ComponentName(context, RankingWidget.class);
        remoteViews.setTextViewText(R.id.tv_widget2, String.valueOf(mModel.mPoints));
        remoteViews.setContentDescription(R.id.tv_widget2, String.valueOf(mModel.mPoints));

        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    private void goToRankingList(String mGroupId) {
        Context context = this;
        Intent intent = new Intent(context, ItemDetailActivity.class);
        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, mGroupId);
        intent.putExtra(ItemDetailFragment.ARG_POINTS, mModel.mRanking.getPoints().toString());
        context.startActivity(intent);

    }

    private void checkLives() {

        switch (mModel.mLives) {
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
        if (mModel.mCountDownTimer != null) {
            mModel.mCountDownTimer.cancel();
        }
        mPbTimer.setProgress(mModel.progressbarCount);
        mModel.mCountDownTimer = new CountDownTimer(mModel.timerMs, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
                mModel.progressbarCount++;
                mPbTimer.setProgress(mModel.progressbarCount);

            }

            @Override
            public void onFinish() {
                //Do what you want
                rankingAddItem(false);
                mModel.progressbarCount++;
                mPbTimer.setProgress(100);
                mModel.mLives -= 1;
                checkLives();
            }
        };
        mModel.mCountDownTimer.start();
    }
}
