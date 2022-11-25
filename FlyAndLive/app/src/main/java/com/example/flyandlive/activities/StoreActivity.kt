package com.example.flyandlive.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.flyandlive.R
import com.example.flyandlive.databinding.ActivityStoreBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class StoreActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityStoreBinding
    private lateinit var reffMoney: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reffMoney = FirebaseDatabase.getInstance().reference.child("Money")

        getMoney()

        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.storeHomeBtn.setOnClickListener { goHome() }
    }

    private fun goHome(){
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_store)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun getMoney() {
        val reffMoney = FirebaseDatabase.getInstance().reference.child("Money")
        reffMoney.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val money = snapshot.child("money").getValue<Int>()!!.toInt()
                binding.moneyStore.text = "$money"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Error, reading money value", error.toException())
            }
        })
    }
}