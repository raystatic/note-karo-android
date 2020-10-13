package com.raystatic.notekaro.other

import android.content.Context
import android.content.SharedPreferences
import com.raystatic.notekaro.other.Constants.KEY_PREF_NAME

const val PRIVATEMODE = 0
class PrefManager(context: Context){

    private var pref:SharedPreferences?=null
    private var editor: SharedPreferences.Editor?=null

    init {
        pref = context.getSharedPreferences(
            KEY_PREF_NAME,
            PRIVATEMODE
        )
    }

    fun saveString(key: String?, data: String?) {
        pref?.edit()?.apply {
            putString(key, data)
        }?.apply()
    }

    fun saveInteger(key: String?, data: Int) {
        pref?.edit()?.apply {
            putInt(key, data)
        }?.apply()
    }

    fun saveBoolean(key: String?, `val`: Boolean?) {
        pref?.edit()?.apply {
            putBoolean(key, `val`!!)
        }?.apply()
    }

    fun getBoolean(key: String?): Boolean? {
        return pref?.getBoolean(key, false)
    }

    fun getString(key: String?): String? {
        return pref?.getString(key, null)
    }

    fun getInt(key: String?): Int? {
        return pref?.getInt(key, 0)
    }

}