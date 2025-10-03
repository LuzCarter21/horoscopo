package com.example.horoscopo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopo.data.Horoscope
import com.example.horoscopo.R
import com.example.horoscopo.utils.SessionManager


class HoroscopeAdapter(var items: List<Horoscope>, val onClickListener: (Int) -> Unit): RecyclerView.Adapter<HoroscopeViewHolder>() {

    var isListView = true

    override fun getItemViewType(position: Int): Int {
        return if (isListView) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder{
        val layoutId = if (viewType == 0) R.layout.item_horoscope else R.layout.item_grid_mode
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
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

    fun updateItems(items: List<Horoscope>){
        this.items = items
        notifyDataSetChanged()
    }
}

    class HoroscopeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nameTextView: TextView = view.findViewById(R.id.nameText)
        val dateTextView: TextView = view.findViewById(R.id.datesText)
        val iconView: ImageView = view.findViewById(R.id.image_icon)
        val favoriteImageView: ImageView = view.findViewById(R.id.selectedFavorite)


        fun render(horoscope: Horoscope) {

            nameTextView.setText(horoscope.name)
            dateTextView.setText(horoscope.dates)
            iconView.setImageResource(horoscope.zodiacIcon)

            val session = SessionManager(itemView.context)
            if (session.isFavorite(horoscope.id)){
                favoriteImageView.visibility = View.VISIBLE
            } else {
                favoriteImageView.visibility = View.GONE
            }

        }


    }