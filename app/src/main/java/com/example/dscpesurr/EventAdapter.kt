package com.example.dscpesurr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.recycler_view_model.view.*

class EventAdapter(options: FirestoreRecyclerOptions<EventModel>) :
        FirestoreRecyclerAdapter<EventModel, EventAdapter.EventAdapterVH>(options)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapterVH {
        return EventAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_model,parent,false))
    }

    override fun onBindViewHolder(holder: EventAdapterVH, position: Int, model: EventModel) {
        holder.eventTitle.text=model.EventTitle
        holder.eventDetails.text=model.EventDetails
        holder.eventDate.text=model.EventDate
    }

    class EventAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var eventTitle=itemView.event_title
        var eventDetails=itemView.event_details
        var eventDate=itemView.event_date
    }
}