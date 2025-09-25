package com.example.horoscopo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopo.data.Horoscope
import com.example.horoscopo.R

class HoroscopeAdapter(val items: List<Horoscope>, val onClickListener: (Int) -> Unit): RecyclerView.Adapter<HoroscopeViewHolder>() {

    //Cual es la vista para los elementos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_horoscope, parent, false)
        return HoroscopeViewHolder(view)
    }


    // Los datos para el elemento
    override fun onBindViewHolder(holder: HoroscopeViewHolder, position: Int) {
        val item = items[position]
        holder.render(item)
        holder.itemView.setOnClickListener {
            onClickListener(position)
        }
    }

        // Cuantos elementos a listar
        override fun getItemCount(): Int {
            return items.size
        }


}

    class HoroscopeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nameTextView: TextView = view.findViewById(R.id.nameText)
        val dateTextView: TextView = view.findViewById(R.id.datesText)
        val iconView: ImageView = view.findViewById(R.id.image_icon)

        fun render(horoscope: Horoscope) {

            nameTextView.setText(horoscope.name)
            dateTextView.setText(horoscope.dates)
            iconView.setImageResource(horoscope.zodiacIcon)

        }

    }