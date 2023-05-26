package com.example.emiapp.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emiapp.R


class AppAdapter : RecyclerView.Adapter<AppAdapter.MyViewholder>() {

    private val dataList = ArrayList<Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.dataitem,
            parent,
            false
        )
        return MyViewholder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = dataList[position]
        holder.tvstartDate.text = currentItem.startDate
        holder.tvstartTime.text = currentItem.startTime
        holder.tvendDate.text = currentItem.endDate
        holder.tvendTime.text = currentItem.endTime
        holder.tvtype.text = currentItem.typeHeadHache
        holder.tvsymptoms.text = currentItem.symptomsHeadHache
        holder.tvpain.text = currentItem.painHeadache
        holder.tvnote.text = currentItem.note
    }

    fun updateDataList(dataList: List<Data>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvstartDate : TextView = itemView.findViewById(R.id.tvstartDate)
        val tvstartTime : TextView = itemView.findViewById(R.id.tvstartTime)
        val tvendDate : TextView = itemView.findViewById(R.id.tvendDate)
        val tvendTime : TextView = itemView.findViewById(R.id.tvendTime)
        val tvtype : TextView = itemView.findViewById(R.id.tvtype)
        val tvsymptoms : TextView = itemView.findViewById(R.id.tvsymptoms)
        val tvpain : TextView = itemView.findViewById(R.id.tvpain)
        val tvnote : TextView = itemView.findViewById(R.id.tvnote)


    }



}