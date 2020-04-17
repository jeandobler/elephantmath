package com.dynamic.dobler.elephantmath.ui

import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.dynamic.dobler.elephantmath.database.entity.Ranking
import com.dynamic.dobler.elephantmath.database.entity.RankingItem
import com.google.firebase.database.DatabaseReference

class ChallengeViewModel : ViewModel() {
    var mRanking: Ranking? = null
    var mRankingItem: RankingItem? = null
    var mRankingItems: ArrayList<RankingItem>? = null
    var mp: MediaPlayer? = null
    var mFirebaseDatabaseReference: DatabaseReference? = null
    var timerMs = 10000
    var progressbarCount = 0
    var mLives = 3
    var mLevel = 2
    var mPoints = 0
    var mNumber1 = 0
    var mNumber2 = 0
    var mResult = 0
    var mCountDownTimer: CountDownTimer? = null
    var saved = false
    var mStarTed = false
}