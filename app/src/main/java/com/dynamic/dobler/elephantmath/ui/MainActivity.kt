package com.dynamic.dobler.elephantmath.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import butterknife.BindView
import butterknife.ButterKnife
import com.dynamic.dobler.elephantmath.R
import com.dynamic.dobler.elephantmath.ui.ranking.ItemListActivity

class MainActivity : BaseActivity() {
    @BindView(R.id.cv_main_ranking)
    var mCvRanking: CardView? = null

    @BindView(R.id.cv_main_challenge)
    var mCvChallenge: CardView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        mCvRanking!!.setOnClickListener { v: View? -> openRanking() }
        mCvChallenge!!.setOnClickListener { v: View? -> openChallenge() }
    }

    protected fun openRanking() {
        val rankingIntent = Intent(this, ItemListActivity::class.java)
        startActivity(rankingIntent)
    }

    protected fun openChallenge() {
        val challengeIntent = Intent(this, ChallengeActivity::class.java)
        startActivity(challengeIntent)
    }
}