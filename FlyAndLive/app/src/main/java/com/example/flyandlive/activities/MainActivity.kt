package com.example.flyandlive.activities

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.flyandlive.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var playBtn: Button
    private lateinit var showMoney: TextView
    private lateinit var infoBtn: ImageButton
    private lateinit var scoreBtn: ImageButton
    private lateinit var storeBtn: ImageButton
    private lateinit var dialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = Dialog(this)

        showMoney = findViewById(R.id.money_main)
        getMoney()

        infoBtn = findViewById(R.id.info_btn)
        infoBtn.setOnClickListener(this)

        playBtn = findViewById(R.id.play_btn)
        playBtn.setOnClickListener(this)

        scoreBtn = findViewById(R.id.star_btn)
        scoreBtn.setOnClickListener(this)

        storeBtn = findViewById(R.id.store_btn)
        storeBtn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.star_btn -> scoreHistory()
            R.id.play_btn -> playGame()
            R.id.info_btn -> openDialog()
            R.id.store_btn -> openStore()
        }
    }

    private fun scoreHistory() {
        intent = Intent(this, ScoreActivity::class.java)
        startActivity(intent)
    }

    private fun openDialog() {
        dialog.setContentView(R.layout.info_layout)
        val close = dialog.findViewById<ImageButton>(R.id.close_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openStore() {
        intent = Intent(this, StoreActivity::class.java)
        startActivity(intent)
    }

    private fun getMoney() {
        val reffMoney = FirebaseDatabase.getInstance().reference.child("Money")
        reffMoney.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val money = snapshot.child("money").getValue<Int>()!!.toInt()
                showMoney.text = "$money"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Error, reading money value", error.toException())
            }
        })
    }

    private fun playGame() {
        Toast.makeText(this, "Game", Toast.LENGTH_SHORT).show()
        intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }


}


