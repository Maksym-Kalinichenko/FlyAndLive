package com.example.flyandlive.activities

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flyandlive.R
import com.example.flyandlive.classes.Score
import com.example.flyandlive.classes.ScoreAdapter
import com.google.firebase.database.*


class ScoreActivity : AppCompatActivity() {
    lateinit var receclerView: RecyclerView
    private lateinit var reff: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        receclerView = findViewById(R.id.score_recyclerView)
        receclerView.layoutManager = LinearLayoutManager(this)

        reff = FirebaseDatabase.getInstance().reference.child("Score")

        val list = ArrayList<Score>()
            reff.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val score = ds.child("score").value.toString().toInt()
                        val data = ds.child("data").value.toString()
                        list.add(Score(score, data))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Error, reading value", error.toException())
                }
            })
        val adapter = ScoreAdapter(list)
        receclerView.adapter = adapter
    }
}