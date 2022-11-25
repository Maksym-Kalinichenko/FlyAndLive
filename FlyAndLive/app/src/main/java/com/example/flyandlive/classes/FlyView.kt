package com.example.flyandlive.classes

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.flyandlive.R
import com.example.flyandlive.activities.FinishActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.floor

class FlyView(context: Context?) : View(context) {

    private var bird = arrayOfNulls<Bitmap>(2)
    private var life = arrayOfNulls<Bitmap>(2)

    private var lifeScore: Int = 3

    private var birdX: Int = 10
    private var birdY: Int = 550
    private var birdSpeed: Int = 0
    private var canvasHeight: Int = 0
    private var canvasWidth: Int = 0
    private var touch: Boolean = false

    private var back: Bitmap
    private var score: Paint = Paint()

    private var candyX: Int = 0
    private var candyY: Int = 0
    private var candySpeed: Int = 10
    private var candy: Bitmap

    private var bellyX: Int = 0
    private var bellyY: Int = 0
    private var bellySpeed: Int = 20
    private var belly: Bitmap

    private var beeX: Int = 0
    private var beeY: Int = 0
    private var beeSpeed: Int = 10
    private var bee: Bitmap

    private var scoreBall: Int = 0

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvasHeight = height
        canvasWidth = width
        back = Bitmap.createScaledBitmap(back, canvasWidth, canvasHeight, true)
        canvas.drawBitmap(back, 0f, 0f, null)//background

        val minBirdY = bird[0]!!.height
        val maxBirdY = canvasHeight - bird[0]!!.height * 3

        birdY += birdSpeed

        if (birdY < minBirdY) {
            birdY = minBirdY
        }
        if (birdY > maxBirdY) {
            birdY = maxBirdY
        }

        birdSpeed += 2

        if (touch) {
            bird[1]?.let { canvas.drawBitmap(it, birdX.toFloat(), birdY.toFloat(), null) }//bird_up
            touch = false
        } else {
            bird[0]?.let {
                canvas.drawBitmap(
                    it,
                    birdX.toFloat(),
                    birdY.toFloat(),
                    null
                )
            }//bird_down
        }

        foodSpam(maxBirdY, minBirdY, canvas)//create food on a screen

        canvas.drawText("Score : $scoreBall", 100f, 100f, score)//score

        for (i in 1..3) {
            val x = (600 + life[0]!!.width * 1.5 * i)
            val y = 40
            if (i <= lifeScore) {
                life[0]?.let { canvas.drawBitmap(it, x.toFloat(), y.toFloat(), null) }//life_full
            } else {
                life[1]?.let { canvas.drawBitmap(it, x.toFloat(), y.toFloat(), null) }//life_miss
            }
        }
    }

    private fun foodSpam(maxBirdY: Int, minBirdY: Int, canvas: Canvas) {
        candyX -= candySpeed
        if (catchItem(candyX, candyY)) {
            scoreBall += 10
            candyX -= 100
        }
        if (candyX < 0) {
            candyX = canvasWidth + 30
            candyY = (floor(Math.random() * (maxBirdY - minBirdY)) + minBirdY).toInt()
        }
        canvas.drawBitmap(candy, candyX.toFloat(), candyY.toFloat(), null)//food_10

        bellyX -= bellySpeed
        if (catchItem(bellyX, bellyY)) {
            scoreBall += 20
            bellyX -= 100
        }
        if (bellyX < 0) {
            bellyX = canvasWidth + 30
            bellyY = (floor(Math.random() * (maxBirdY - minBirdY)) + minBirdY).toInt()
        }
        canvas.drawBitmap(belly, bellyX.toFloat(), bellyY.toFloat(), null)//food_20

        beeX -= beeSpeed
        if (catchItem(beeX, beeY)) {
            lifeScore--
            if (lifeScore == 0) {
                val intent = Intent(context, FinishActivity::class.java)
                intent.putExtra("score", scoreBall)
                context.startActivity(intent)
            }
            beeX -= 100
        }
        if (beeX < 0) {
            beeX = canvasWidth + 30
            beeY = (floor(Math.random() * (maxBirdY - minBirdY)) + minBirdY).toInt()
        }
        canvas.drawBitmap(bee, beeX.toFloat(), beeY.toFloat(), null)//danger_1
    }

    private fun catchItem(x: Int, y: Int): Boolean {
        if (birdX < x && x < (birdX + bird[0]!!.width) && birdY < y && y < (birdY + bird[0]!!.height)) {
            return true
        }
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            touch = true
            birdSpeed = -25
        }
        return true
    }

    init {
        back = BitmapFactory.decodeResource(resources, R.drawable.back1)
        bird[0] = BitmapFactory.decodeResource(resources, R.drawable.bird_1)//bird_down
        bird[1] = BitmapFactory.decodeResource(resources, R.drawable.bird_2)//bird_up
        life[0] = BitmapFactory.decodeResource(resources, R.drawable.life_full)
        life[1] = BitmapFactory.decodeResource(resources, R.drawable.life_miss)

        val newSize = resources.getDimensionPixelSize(R.dimen.fontSize).toFloat()
        score.color = Color.RED
        score.textSize = newSize
        score.typeface = Typeface.DEFAULT_BOLD
        score.isAntiAlias = true

        candy = BitmapFactory.decodeResource(resources, R.drawable.candy1)//food_10
        belly = BitmapFactory.decodeResource(resources, R.drawable.candy2)//food_20

        bee = BitmapFactory.decodeResource(resources, R.drawable.bee)//danger_1
        checkBack()
        checkBird()
    }

    private fun checkBack() {
        val reffBack = FirebaseDatabase.getInstance().reference.child("Landscapes")
        reffBack.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val select = ds.child("select").value.toString().toInt()
                    val idLand = ds.child("id").value.toString().toInt()
                    if (select == 1) {
                        makeBack(idLand)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Error, reading value", error.toException())
            }
        })
    }

    private fun checkBird() {
        val reffBirds = FirebaseDatabase.getInstance().reference.child("Birds")
        reffBirds.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val select = ds.child("select").value.toString().toInt()
                    val idBird = ds.child("id").value.toString().toInt()
                    if (select == 1) {
                        makeBird(idBird)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Error, reading value", error.toException())
            }
        })
    }

    private fun makeBack(idLand: Int) {
        when (idLand) {
            1 -> back = BitmapFactory.decodeResource(resources, R.drawable.back1)
            2 -> back = BitmapFactory.decodeResource(resources, R.drawable.back2)
        }
    }

    private fun makeBird(idBird: Int) {
        when (idBird) {
            1 -> {
                bird[0] = BitmapFactory.decodeResource(resources, R.drawable.bird_1)//bird_down
                bird[1] = BitmapFactory.decodeResource(resources, R.drawable.bird_2)//bird_up
            }
            2 -> {
                bird[0] = BitmapFactory.decodeResource(resources, R.drawable.bird3)//bird_down
                bird[1] = BitmapFactory.decodeResource(resources, R.drawable.bird4)//bird_up
            }
        }
    }
}



