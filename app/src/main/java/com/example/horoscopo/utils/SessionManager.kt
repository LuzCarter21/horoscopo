package com.example.horoscopo.utils

import android.content.Context

class SessionManager(context: Context) {

    val sharedPreferences = context.getSharedPreferences("horoscope.session", Context.MODE_PRIVATE)


    fun setFavorite(horoscopeId: String){

        val editor =sharedPreferences.edit()
        editor.putString("FAVORITE_HOROSCOPE_ID", horoscopeId)
        editor.apply()
    }

    fun getFavorite(): String{
        return sharedPreferences.getString("FAVORITE_HOROSCOPE_ID", "")!!

    }




    fun isFavorite(horoscopeId: String): Boolean {

        return horoscopeId == getFavorite()

    }





}