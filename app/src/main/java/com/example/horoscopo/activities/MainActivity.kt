package com.example.horoscopo.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopo.R
import com.example.horoscopo.adapters.HoroscopeAdapter
import com.example.horoscopo.data.Horoscope
import com.example.horoscopo.utils.SessionManager
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HoroscopeAdapter

    lateinit var session: SessionManager
    lateinit var horoscopeList: List<Horoscope>

    lateinit var toolbar: MaterialToolbar

    var isListView = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.topAppBar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Toolbar

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Horoscope"
            setDisplayShowTitleEnabled(true)
        }
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        session = SessionManager(this)
        horoscopeList = Horoscope.getAll()

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        horoscopeList = getHoroscopeListOrdered()
        adapter = HoroscopeAdapter(horoscopeList, ::onItemClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


    }




    override fun onResume() {
        super.onResume()

        adapter.updateItems(horoscopeList)
        refreshList()
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

    // Función para recorrer la jerarquía y encontrar el EditText del SearchView
    private fun getSearchEditText(searchView: SearchView): EditText? {
        fun findEditText(viewGroup: ViewGroup): EditText? {
            for (i in 0 until viewGroup.childCount) {
                val child = viewGroup.getChildAt(i)
                if (child is EditText) return child
                if (child is ViewGroup) {
                    val result = findEditText(child)
                    if (result != null) return result
                }
            }
            return null
        }

        for (i in 0 until searchView.childCount) {
            val child = searchView.getChildAt(i)
            if (child is EditText) return child
            if (child is ViewGroup) {
                val result = findEditText(child)
                if (result != null) return result
            }
        }
        return null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu?.findItem(R.id.searchIcon)
        val searchView = searchItem?.actionView as? SearchView


        val searchEditText = getSearchEditText(searchView!!)
        searchEditText?.setTextColor(ContextCompat.getColor(this, R.color.white))
        searchEditText?.setHintTextColor(ContextCompat.getColor(this, R.color.gray))


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("SEARCH", "onQueryTextSubmit: $query")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                horoscopeList = Horoscope.getAll().filter {
                    getString(it.name).contains(newText, true)
                    ||getString(it.dates).contains(newText, true)
                }

                adapter.updateItems(horoscopeList)
                return true
            }
        })

        return true
    }

    fun getHoroscopeListOrdered(): List<Horoscope> {
        val allHoroscopes = Horoscope.getAll().toMutableList()
        val favoriteId = session.getFavorite()

        val favoriteIndex = allHoroscopes.indexOfFirst { it.id == favoriteId}
        if (favoriteIndex != -1){
            val favorite = allHoroscopes.removeAt(favoriteIndex)
            allHoroscopes.add(0, favorite)
        }
        return allHoroscopes
    }

    fun refreshList() {
        horoscopeList = getHoroscopeListOrdered()
        adapter.updateItems(horoscopeList)
    }


    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.listmode -> {
                // Alterna el estado
                val isCurrentlyList = adapter.isListView
                adapter.isListView = !isCurrentlyList

                // Notificar al adapter primero
                adapter.notifyDataSetChanged()

                // Cambia layout manager
                recyclerView.layoutManager = if (adapter.isListView) {
                    androidx.recyclerview.widget.LinearLayoutManager(this)
                } else {
                    androidx.recyclerview.widget.GridLayoutManager(this, 2)
                }

                // Cambia icono
                item.setIcon(if (adapter.isListView) R.drawable.grid_view else R.drawable.mode_list)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
