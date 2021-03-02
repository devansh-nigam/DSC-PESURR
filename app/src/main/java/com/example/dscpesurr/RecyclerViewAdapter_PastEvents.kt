package com.example.dscpesurr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter_PastEvents: RecyclerView.Adapter<RecyclerViewAdapter_PastEvents.ViewHolder>()
{
    private val itemTitles = arrayOf("Android Study Jams: Prior Programming Experience","Arrays & Bit Manipulation","Android Study Jams: New To Programming","30 Days Of Google Cloud")
    private val itemDetails = arrayOf("Android Study Jams are community-organized study groups for people to learn how to build Android apps.",
    "Are you someone who struggles with time complexity of various searching and sorting?\nArrays and Bit Manipulation are topics which can be a bit tricky in Competitive Programming.",
        "Android Study Jams are community-organized study groups for people to learn how to build Android apps.",
    "The introductory session to commence 30 Days of Google Cloud will give you an overview of Google Cloud Platform tools where you can earn skill badge and learn its applications.")
    private val itemImages=arrayOf(R.drawable.androidjam,R.drawable.tcc,R.drawable.androidjam,R.drawable.cloud)



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var eventImage: ImageView
        var eventTitle: TextView
        var eventDetails: TextView

        init{
            eventImage=itemView.findViewById<ImageView>(R.id.event_image)
            eventTitle=itemView.findViewById<TextView>(R.id.event_title)
            eventDetails=itemView.findViewById<TextView>(R.id.event_details)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_model,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.eventTitle.text=itemTitles[position]
        holder.eventDetails.text=itemDetails[position]
        holder.eventImage.setImageResource(itemImages[position])

        holder.itemView.setOnClickListener {
            Toast.makeText(it.context,"Clicked on the item", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return itemTitles.size
    }
}