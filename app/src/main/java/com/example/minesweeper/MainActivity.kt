package com.example.minesweeper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.minesweeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MineSweeperView.OnScoreChangeListener,
    MineSweeperView.OnGameEndListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MineSweeperViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val highScore = sharedPrefs.getInt("highScore", 0)
        viewModel.updateHighScore(highScore)

        binding.tvHighScore.text = getString(R.string.high_score_text, highScore)
        binding.tvScore.text = getString(R.string.score, 0)

        viewModel.score.observe(this) { score ->
            binding.tvScore.text = getString(R.string.score, score)
        }

        viewModel.elapsedTime.observe(this) { elapsedTime ->
            binding.tvTimer.text = String.format(getString(R.string.time), elapsedTime)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            resetGame()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.mineSweeperView.setOnScoreChangeListener(this)
        binding.mineSweeperView.setOnGameEndListener(this)

        viewModel.startTimer()

        binding.btnTryAgain.visibility = View.GONE
    }

    private fun resetGame() {
        binding.mineSweeperView.resetGame()
        viewModel.resetGame()
        binding.btnTryAgain.visibility = View.GONE
    }

    override fun onScoreChanged(score: Int) {
        viewModel.updateScore(score)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopTimer()
    }

    override fun onGameEnd() = with(binding) {
        viewModel.stopTimer()

        val score = mineSweeperView.getCurrentScore()
        val highScore = viewModel.highScore.value ?: 0
        if (score > highScore) {
            viewModel.updateHighScore(score)
            val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            sharedPrefs.edit().putInt("highScore", score).apply()

            tvHighScore.text = String.format(getString(R.string.high_score_text), score)
        }
        animationView.visibility = View.VISIBLE
        animationView.playAnimation()

        binding.btnTryAgain.visibility = View.VISIBLE
        binding.btnTryAgain.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}