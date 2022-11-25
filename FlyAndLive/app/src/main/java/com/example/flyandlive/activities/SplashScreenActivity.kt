package com.example.flyandlive.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.flyandlive.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenActivity : AppCompatActivity() {
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        progressBar = findViewById(R.id.progress_Bar)

        GlobalScope.launch {
            doWork()
            startApp()
        }
    }

    private suspend fun doWork() {
        for (i in 0..100 step 1) {
            delay(50)
            progressBar?.progress = i
        }
    }


    private fun startApp() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_shrink_enter, R.anim.anim_shrink_exit)
    }
}