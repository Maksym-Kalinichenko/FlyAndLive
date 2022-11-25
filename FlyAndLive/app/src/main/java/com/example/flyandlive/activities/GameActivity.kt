package com.example.flyandlive.activities

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.flyandlive.classes.FlyView
import java.util.*


class GameActivity : AppCompatActivity() {
    lateinit var flyView: FlyView
    private var handler: Handler = Handler()
    private val interval: Long = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flyView = FlyView(this)
        setContentView(flyView)

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post { flyView.invalidate() }
            }
        }, 0, interval)
    }
}

