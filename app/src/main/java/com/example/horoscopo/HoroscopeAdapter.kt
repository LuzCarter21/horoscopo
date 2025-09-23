package com.example.horoscopo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HoroscopeAdapter(val items: List<Horoscope>): RecyclerView.Adapter<HoroscopeViewHolder>() {

    //Cual es la vista para los elementos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horoscope,parent, false)
        return HoroscopeViewHolder(view)
    }
    // Los datos para el elemento
    override fun onBindViewHolder(holder: HoroscopeViewHolder, position: Int) {
        val item = items[position]
    }

    // Cuantos elementos a listar
    override fun getItemCount(): Int {
        return items.size
    }

}

class HoroscopeViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val nameTextView: TextView = view.findViewById()
    val dateTextView: TextView = view.findViewById()
    val iconView: ImageView = view.findViewById()

}