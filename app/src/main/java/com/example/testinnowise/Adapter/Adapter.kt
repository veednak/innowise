package com.example.innowise.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testinnowise.R

import kotlinx.android.synthetic.main.row.view.*

//
class MyAdapter(private val exampleList: List<ModelWeather?>) :
    RecyclerView.Adapter<MyAdapter.ExampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.row,
            parent, false
        )
        return ExampleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.imageView.setImageResource(currentItem!!.Img)
        holder.textView1.text = currentItem.Time
        holder.textView2.text = currentItem.Weather_Now
        holder.textView3.text = currentItem.Temp.toString()
    }

    override fun getItemCount() = exampleList.size
    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.weatherImage
        val textView1: TextView = itemView.timeValue
        val textView2: TextView = itemView.weatherNow
        val textView3: TextView = itemView.temperature
    }
}