package com.example.horoscopo.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopo.R
import com.example.horoscopo.adapters.HoroscopeAdapter
import com.example.horoscopo.data.Horoscope
import com.google.android.material.appbar.MaterialToolbar
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val horoscopeList: List<Horoscope> = Horoscope.getAll()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuración de Toolbar como ActionBar
        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Horoscope"
            setDisplayShowTitleEnabled(true)
        }

        toolbar.setTitleTextColor(getColor(R.color.white))


        recyclerView = findViewById(R.id.recyclerView)
        val adapter = HoroscopeAdapter(horoscopeList, ::onItemClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchView = menu!!.findItem(R.id.searchIcon).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("SEARCH", "onQueryTextSubmit: $query")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // TODO: Fliltrar la lista y mostrarlas
                return true
            }
        })

        return true
    }


    private fun onItemClickListener(position: Int) {
        val horoscope = horoscopeList[position]
        goToDetail(horoscope)
    }

    private fun goToDetail(horoscope: Horoscope) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("HOROSCOPE_ID", horoscope.id)
        startActivity(intent)
    }
}