package com.example.horoscopo.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.horoscopo.R
import com.example.horoscopo.data.Horoscope
import com.example.horoscopo.utils.SessionManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailActivity : AppCompatActivity() {

    lateinit var nameZodiac: TextView
    lateinit var datesZodiac: TextView

    lateinit var imageZodiac: ImageView

    lateinit var horoscope: Horoscope

    lateinit var session: SessionManager

    lateinit var favoriteMenuItem: MenuItem

    lateinit var luckyText: TextView
    lateinit var  progressBar: LinearProgressIndicator

    var isFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)

        val toolbar: MaterialToolbar = findViewById(R.id.topAppBarDetail)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Detail"
            setDisplayShowTitleEnabled(true)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))


        nameZodiac = findViewById(R.id.zodiacName)
        datesZodiac = findViewById(R.id.zodiacDate)
        imageZodiac = findViewById(R.id.zodiacImage)
        luckyText = findViewById(R.id.luckyTextView)
        progressBar = findViewById(R.id.progressIndicator)

        val id = intent.getStringExtra("HOROSCOPE_ID")!!

        horoscope = Horoscope.getByID(id)



        supportActionBar?.setTitle(horoscope.name)



        horoscope = Horoscope.getByID(id)

        isFavorite = session.isFavorite(horoscope.id)


        nameZodiac.setText(horoscope.name)
        datesZodiac.setText(horoscope.dates)
        imageZodiac.setImageResource(horoscope.zodiacIcon)

        getHoroscopeLuck()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu,menu)

        favoriteMenuItem = menu.findItem(R.id.favoriteIcon)
        setFavoriteIcon()

        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.favoriteIcon -> {
                isFavorite = !isFavorite
                if (isFavorite) {
                    session.setFavorite(horoscope.id)
                } else {
                    session.setFavorite("")
                }
                setFavoriteIcon()

                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    fun setFavoriteIcon() {
        if (isFavorite) {
            favoriteMenuItem.setIcon(R.drawable.favorite_icon_menu_selected)
        } else {
            favoriteMenuItem.setIcon(R.drawable.favorite_icon_menu)
        }
    }

    fun getHoroscopeLuck() {
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            val url = URL("https://horoscope-app-api.vercel.app/api/v1/get-horoscope/daily?sign=${horoscope.id}")

            // HTTP Connexion
            val urlConnection = url.openConnection() as HttpsURLConnection

            // Method
            urlConnection.requestMethod = "GET"

            try {
                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response
                    val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = StringBuffer()
                    var inputLine: String? = null

                    while ((bufferedReader.readLine().also { inputLine = it }) != null) {
                        response.append(inputLine)
                    }
                    bufferedReader.close()

                    val result = JSONObject(response.toString()).getJSONObject("data").getString("horoscope_data")

                    CoroutineScope(Dispatchers.Main).launch {
                        progressBar.visibility = View.GONE
                        luckyText.text = result
                    }
                } else {
                    Log.i("API", "Hubo un error en la llamada al API")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                urlConnection.disconnect()
            }
        }
    }


}