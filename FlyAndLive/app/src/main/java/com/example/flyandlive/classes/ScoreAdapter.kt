package com.example.flyandlive.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flyandlive.R

class ScoreAdapter(private val mList: List<Score>) :
    RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_score, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val list = mList[position]

        holder.data.text = list.data
        holder.score.text = list.score.toString()

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val data: TextView = itemView.findViewById(R.id.data_score)
        val score: TextView = itemView.findViewById(R.id.score_star)
    }
}
