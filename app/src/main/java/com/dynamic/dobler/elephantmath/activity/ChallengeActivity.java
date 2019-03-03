package com.dynamic.dobler.elephantmath.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.database.entity.Ranking;
import com.dynamic.dobler.elephantmath.database.entity.RankingItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChallengeActivity extends BaseActivity {

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


    private int progressbarCount = 0;
    private int mLives = 3;
    private int mLevel = 2;
    private int mPoints = 0;
    private int mNumber1;
    private int mNumber2;
    private int mResult;
    private CountDownTimer mCountDownTimer;
    private MediaPlayer mp;
    private DatabaseReference mFirebaseDatabaseReference;
//    private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>
//            mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        ButterKnife.bind(this);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();



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
        Log.e(this.mGoogleId,this.mGoogleId);
        mRanking = new Ranking(this.mGoogleId, date, 0);
        mRankingItems = new ArrayList<RankingItem>();


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
                    if (mPoints % 6 == 0) {
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
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();

            }

            ;
        });

        RankingItem newItem = new RankingItem(mNumber1, mNumber2, (float) progressbarCount / 10, isCorrect);

        mRankingItems.add(newItem);
    }

    private void startLevel() {

        mTvLevel.setText("x" + String.valueOf(mLevel));
        mEtKeyboard.setText("");

        Random rand = new Random();

        mNumber1 = rand.nextInt(mLevel > 10 ? mLevel - 2 : 7) + 2;
        mNumber2 = mLevel;
        mResult = mNumber1 * mNumber2;
        mTvProblem.setText(String.valueOf(mNumber1) + " x " + String.valueOf(mNumber2) + " =");

        progressbarCount = 0;
        progressbarTimer();

    }


    private void endGame() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mTvProblem.setText("u Loose");
        mRanking.setRankingItems(mRankingItems);
        mRanking.setPoints(-mPoints);
        mFirebaseDatabaseReference.child("ranking")
                .push().setValue(mRanking);


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
