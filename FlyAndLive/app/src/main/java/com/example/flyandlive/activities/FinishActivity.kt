package com.example.flyandlive.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flyandlive.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FinishActivity : AppCompatActivity() {

    private lateinit var score: TextView
    private lateinit var restartBtn: ImageButton
    private lateinit var homeBtn: Button
    private lateinit var moneyShow: TextView
    private var scoreGame: String = ""
    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        moneyShow = findViewById(R.id.money)
        score = findViewById(R.id.text_score)
        scoreGame = intent.extras!!.get("score").toString()
        score.text = "Your score : $scoreGame"
        saveDB()

        restartBtn = findViewById(R.id.restart_btn)
        restartBtn.setOnClickListener {
            restartGame()
        }

        homeBtn = findViewById(R.id.home_btn)
        homeBtn.setOnClickListener {
            mainScreen()
        }
    }

    private fun saveDB() {
        val formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val timeNow = LocalDateTime.now().format(formatTime)
        getMoney()
        val reffScore = FirebaseDatabase.getInstance().reference.child("Score").push()
        reffScore.child("score").setValue(scoreGame)
        reffScore.child("data").setValue(timeNow)
    }
    private fun getMoney() {
        val reffMoney = FirebaseDatabase.getInstance().reference.child("Money")
        reffMoney.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val money = snapshot.child("money").getValue<Int>()!!.toInt()
                moneyShow.text = "$money"
                if (count < 1)
                    setMoney(money, reffMoney)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Error, reading money value", error.toException())
            }
        })
    }
    private fun setMoney(money: Int, reffMoney: DatabaseReference) {
        val newMoney: Int = money + (scoreGame.toInt() / 10)
        moneyShow.text = "$newMoney"
        reffMoney.child("money").setValue(newMoney)
        count++
    }

    private fun restartGame() {
        Toast.makeText(this, "Game", Toast.LENGTH_SHORT).show()
        intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    private fun mainScreen() {
        Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}