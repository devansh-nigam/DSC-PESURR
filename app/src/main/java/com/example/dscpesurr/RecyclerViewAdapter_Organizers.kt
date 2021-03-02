package com.example.dscpesurr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter_Organizers : RecyclerView.Adapter<RecyclerViewAdapter_Organizers.ViewHolder>()
{
    private val itemTitles = arrayOf("Adarsh Shetty",
        "Shubham Gupta",
        "Sparsh Temani",
        "Rishith Bhowmick",
        "Pranav Bhatt",
        "Bhargav SNV",
        "Khushei Meda",
        "Sakshi Shetty",
        "Saarim Khursheed",
        "Nikhil Krishnadass",
        "Devansh Nigam",
        "Chakita M",
        "Supreeth Kurpad"
    )


    private val itemDetails = arrayOf("DSC Lead",
        "Head Of App Development",
        "Co-Head of ML",
        "Co-Head of ML",
        "Head Of Cloud",
        "Cloud",
        "Head Of Web Development",
        "Head of Logistics and Outreach",
        "Logistics and Outreach",
        "Head of Design",
        "App Development",
        "Machine Learning",
        "Web Development"
    )

    private val itemImages=arrayOf(R.drawable.adarsh,
        R.drawable.shubham,
        R.drawable.sparsh,
        R.drawable.rishith,
        R.drawable.pranav,
        R.drawable.bhargav,
        R.drawable.khushei,
        R.drawable.sakshi,
        R.drawable.saarim,
        R.drawable.nikhil,
        R.drawable.devansh,
        R.drawable.chakita,
        R.drawable.supreeth
        )



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var organizer_image: ImageView
        var organizer_name: TextView
        var organizer_post: TextView

        init{
            organizer_image=itemView.findViewById<ImageView>(R.id.organizer_image)
            organizer_name=itemView.findViewById<TextView>(R.id.organizer_name)
            organizer_post=itemView.findViewById<TextView>(R.id.organizer_post)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_model_organizer,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.organizer_name.text=itemTitles[position]
        holder.organizer_post.text=itemDetails[position]
        holder.organizer_image.setImageResource(itemImages[position])

        holder.itemView.setOnClickListener {
            Toast.makeText(it.context,"Clicked on the item", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return itemTitles.size
    }
}