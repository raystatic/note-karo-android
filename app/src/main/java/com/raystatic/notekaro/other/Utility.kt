package com.raystatic.notekaro.other

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.raystatic.notekaro.R

object Utility{

    fun showSnack(view: View, msg:String, context:Context){
        val snackBar = Snackbar.make(view,msg, Snackbar.LENGTH_SHORT)
        snackBar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.lime))
    }

    fun showToast(msg:String, context:Context){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }

}