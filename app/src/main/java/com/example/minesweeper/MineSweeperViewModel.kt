package com.example.minesweeper

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MineSweeperViewModel : ViewModel() {

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _highScore = MutableLiveData(0)
    val highScore: LiveData<Int> get() = _highScore

    private val _elapsedTime = MutableLiveData(0)
    val elapsedTime: LiveData<Int> get() = _elapsedTime

    private var timerHandler: Handler? = null
    private var timerRunnable: Runnable? = null

    fun startTimer() {
        timerHandler = Handler()
        timerRunnable = object : Runnable {
            override fun run() {
                _elapsedTime.value = (_elapsedTime.value ?: 0) + 1
                timerHandler?.postDelayed(this, 1000)
            }
        }
        timerHandler?.post(timerRunnable!!)
    }

    fun stopTimer() {
        timerHandler?.removeCallbacks(timerRunnable!!)
        timerHandler = null
        timerRunnable = null
    }

    fun updateScore(newScore: Int) {
        _score.value = newScore
    }

    fun updateHighScore(newHighScore: Int) {
        _highScore.value = newHighScore
    }

    fun resetGame() {
        _score.value = 0
        _elapsedTime.value = 0
    }
}
