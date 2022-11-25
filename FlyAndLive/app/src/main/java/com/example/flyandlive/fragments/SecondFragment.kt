package com.example.flyandlive.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.flyandlive.R
import com.example.flyandlive.databinding.FragmentSecondBinding
import com.google.firebase.database.*

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.landscapesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        checkPrice()
        binding.bird1.setOnClickListener {
            checkLand("Bird1")
        }
        binding.bird2.setOnClickListener {
            checkLand("Bird2")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkLand(name: String) {
        val reff = FirebaseDatabase.getInstance().reference.child("Birds")
        reff.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val buy = snapshot.child(name).child("buy").value.toString().toInt()
                val price = snapshot.child(name).child("price").value.toString().toInt()
                if (buy == 1) {
                    Toast.makeText(
                        context,
                        "You have this bird. Select is ON",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    changeSelect(name, reff)
                } else {
                    val reffMoney =
                        FirebaseDatabase.getInstance().reference.child("Money").child("money")
                    reffMoney.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(sh: DataSnapshot) {
                            var money = sh.value.toString().toInt()
                            if (money >= price) {
                                Toast.makeText(
                                    context,
                                    "My congratulations this bird is yours",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                money -= price
                                reffMoney.setValue(money)
                                reff.child(name).child("buy").setValue(1)
                                reff.child(name).child("price").setValue(0)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Sorry, You don't have enough money",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.w(
                                ContentValues.TAG,
                                "Error, reading value",
                                error.toException()
                            )
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Error, reading value", error.toException())
            }
        })

    }

    private fun changeSelect(mainName: String, reff: DatabaseReference) {
        when (mainName) {
            "Bird1" -> {
                reff.child(mainName).child("select").setValue(1)
                reff.child("Bird2").child("select").setValue(0)
            }
            "Bird2" -> {
                reff.child(mainName).child("select").setValue(1)
                reff.child("Bird1").child("select").setValue(0)
            }
        }
    }

    private fun checkPrice() {
        val reffNew = FirebaseDatabase.getInstance().reference.child("Birds")
        reffNew.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val idBird = ds.child("id").value.toString().toInt()
                    val price = ds.child("price").value.toString()
                    when (idBird) {
                        1 -> binding.priceBird1.text = price
                        2 -> binding.priceBird2.text = price
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Error, reading value", error.toException())
            }
        })
    }


}