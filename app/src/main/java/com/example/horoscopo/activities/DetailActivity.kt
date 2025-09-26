package com.example.horoscopo.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.horoscopo.R
import com.example.horoscopo.data.Horoscope

class DetailActivity : AppCompatActivity() {

    lateinit var nameZodiac: TextView
    lateinit var datesZodiac: TextView

    lateinit var imageZodiac: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nameZodiac = findViewById(R.id.zodiacName)
        datesZodiac = findViewById(R.id.zodiacDate)
        imageZodiac = findViewById(R.id.zodiacImage)

        val id = intent.getStringExtra("HOROSCOPE_ID")!!

        val horoscope = Horoscope.getByID(id)

        nameZodiac.setText(horoscope.name)
        datesZodiac.setText(horoscope.dates)
        imageZodiac.setImageResource(horoscope.zodiacIcon)

    }
}