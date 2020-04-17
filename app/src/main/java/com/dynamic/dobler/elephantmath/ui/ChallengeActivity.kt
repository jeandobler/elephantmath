package com.dynamic.dobler.elephantmath.ui

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.dynamic.dobler.elephantmath.R
import com.dynamic.dobler.elephantmath.database.entity.Ranking
import com.dynamic.dobler.elephantmath.database.entity.RankingItem
import com.dynamic.dobler.elephantmath.ui.ranking.ItemDetailActivity
import com.dynamic.dobler.elephantmath.ui.ranking.ItemDetailFragment
import com.dynamic.dobler.elephantmath.widget.RankingWidget
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class ChallengeActivity : BaseActivity() {
    var TAG = "challengeActivity"

    @BindView(R.id.pb_challenge_timer)
    var mPbTimer: ProgressBar? = null

    @BindView(R.id.et_challenge_keyboard)
    var mEtKeyboard: EditText? = null

    @BindView(R.id.tv_challenge_problem)
    var mTvProblem: TextView? = null

    @BindView(R.id.tv_challenge_level)
    var mTvLevel: TextView? = null

    @BindView(R.id.iv_challenge_heart1)
    var mHeart1: ImageView? = null

    @BindView(R.id.iv_challenge_heart2)
    var mHeart2: ImageView? = null

    @BindView(R.id.iv_challenge_heart3)
    var mHeart3: ImageView? = null
    private var mModel: ChallengeViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)
        ButterKnife.bind(this)
        mModel = ViewModelProviders.of(this).get(ChallengeViewModel::class.java)
        mModel!!.mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
    }

    override fun onStart() {
        super.onStart()
        if (!mModel!!.mStarTed) {
            prepareDatabase()
            startLevel()
            mModel!!.mStarTed = true
        } else {
            continueLevel()
        }
        observeChallengeEditText()
    }

    override fun onPause() {
        super.onPause()
        if (mModel!!.mCountDownTimer != null) {
            mModel!!.mCountDownTimer!!.cancel()
        }
        if (mModel!!.mp != null) {
            mModel!!.mp!!.release()
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        endGame()
        super.onBackPressed()
    }

    fun prepareDatabase() {
        val date = Date()
        mModel!!.mRanking =
            Ranking(mGoogleId, createdAt = date, points = 0)
        mModel!!.mRankingItems = ArrayList()
    }

    fun observeChallengeEditText() {
        mEtKeyboard!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString() == java.lang.String.valueOf(mModel!!.mResult)) {
                    mModel!!.mPoints++
                    if (mModel!!.mPoints % 3 == 0) {
                        mModel!!.mLevel++
                        if (mModel!!.mLives < 3) {
                            mModel!!.mLives++
                            checkLives()
                        } else {
                            mModel!!.mPoints++
                        }
                    }
                    rankingAddItem(true)
                    startLevel()
                }
            }
        })
    }

    private fun rankingAddItem(isCorrect: Boolean) {
        mModel!!.mp = MediaPlayer.create(
            applicationContext,
            if (isCorrect) R.raw.level_up else R.raw.mktoasty
        )
        mModel!!.mp?.start()
        mModel!!.mp?.setOnCompletionListener(OnCompletionListener { obj: MediaPlayer -> obj.release() })
        val newItem = RankingItem(
            number1 = mModel!!.mNumber1,
            number2 = mModel!!.mNumber2,
            speed = mModel!!.progressbarCount.toFloat() / 10,
            isCorrect = isCorrect
        )
        mModel!!.mRankingItems?.add(newItem)
    }

    private fun startLevel() {
        mEtKeyboard!!.setText("")
        val rand = Random()
        mModel!!.mNumber1 = rand.nextInt(if (mModel!!.mLevel > 10) mModel!!.mLevel - 2 else 7) + 2
        continueLevel()
        mModel!!.progressbarCount = 0
        progressbarTimer()
    }

    private fun continueLevel() {
        val levelText = "x" + java.lang.String.valueOf(mModel!!.mLevel)
        mTvLevel!!.text = levelText
        mTvLevel!!.contentDescription = levelText
        mModel!!.mNumber2 = mModel!!.mLevel
        mModel!!.mResult = mModel!!.mNumber1 * mModel!!.mNumber2
        val problemText =
            java.lang.String.valueOf(mModel!!.mNumber1) + " x " + java.lang.String.valueOf(mModel!!.mNumber2) + " ="
        mTvProblem!!.text = problemText
        mTvProblem!!.contentDescription = problemText
        progressbarTimer()
    }

    private fun endGame() {
        if (mModel!!.mCountDownTimer != null) {
            mModel!!.mCountDownTimer!!.cancel()
        }
        mTvProblem!!.setText(R.string.game_over)
        mTvProblem!!.contentDescription = getString(R.string.game_over)
        mTvProblem!!.textSize = R.dimen.title5Text.toFloat()
        mModel!!.mRanking!!.rankingItems = mModel!!.mRankingItems
        mModel!!.mRanking!!.points = -mModel!!.mPoints
        val mGroupId: String
        if (!mModel!!.saved) {
            val pushedPostRef =
                mModel!!.mFirebaseDatabaseReference!!.child("ranking").push()
            mGroupId = pushedPostRef.key.toString()
            pushedPostRef.setValue(mModel!!.mRanking)
            sendWidgetBroadCast()
            goToRankingList(mGroupId)
            mModel!!.saved = true
        }
    }

    private fun sendWidgetBroadCast() {
        val context: Context = this
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val remoteViews =
            RemoteViews(context.packageName, R.layout.widget_ranking)
        val thisWidget = ComponentName(context, RankingWidget::class.java)
        remoteViews.setTextViewText(R.id.tv_widget2, java.lang.String.valueOf(mModel!!.mPoints))
        remoteViews.setContentDescription(
            R.id.tv_widget2,
            java.lang.String.valueOf(mModel!!.mPoints)
        )
        appWidgetManager.updateAppWidget(thisWidget, remoteViews)
    }

    private fun goToRankingList(mGroupId: String?) {
        val context: Context = this
        val intent = Intent(context, ItemDetailActivity::class.java)
        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, mGroupId)
        intent.putExtra(ItemDetailFragment.ARG_POINTS, mModel!!.mRanking!!.points.toString())
        context.startActivity(intent)
    }

    private fun checkLives() {
        when (mModel!!.mLives) {
            3 -> {
                mHeart1!!.visibility = View.VISIBLE
                mHeart2!!.visibility = View.VISIBLE
                mHeart3!!.visibility = View.VISIBLE
                startLevel()
            }
            2 -> {
                mHeart1!!.visibility = View.VISIBLE
                mHeart2!!.visibility = View.VISIBLE
                mHeart3!!.visibility = View.INVISIBLE
                startLevel()
            }
            1 -> {
                mHeart1!!.visibility = View.VISIBLE
                mHeart2!!.visibility = View.INVISIBLE
                mHeart3!!.visibility = View.INVISIBLE
                startLevel()
            }
            0 -> endGame()
        }
    }

    private fun progressbarTimer() {
        if (mModel!!.mCountDownTimer != null) {
            mModel!!.mCountDownTimer!!.cancel()
        }
        mPbTimer!!.progress = mModel!!.progressbarCount
        mModel!!.mCountDownTimer = object : CountDownTimer(mModel!!.timerMs.toLong(), 100) {
            override fun onTick(millisUntilFinished: Long) {
                mModel!!.progressbarCount++
                mPbTimer!!.progress = mModel!!.progressbarCount
            }

            override fun onFinish() {
                //Do what you want
                rankingAddItem(false)
                mModel!!.progressbarCount++
                mPbTimer!!.progress = 100
                mModel!!.mLives -= 1
                checkLives()
            }
        }
        mModel!!.mCountDownTimer?.start()
    }
}