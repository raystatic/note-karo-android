package com.raystatic.notekaro.other

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.internal.bind.util.ISO8601Utils.format
import com.raystatic.notekaro.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Utility{

    fun showSnack(view: View, msg:String, context:Context){
        val snackBar = Snackbar.make(view,msg, Snackbar.LENGTH_SHORT)
        snackBar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.lime))
    }

    fun showToast(msg:String, context:Context){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }

    fun getFormattedDateFromISO(str:String): String {
        val sdf = DateFormat.getDateTimeInstance(
            DateFormat.FULL,
            DateFormat.FULL
        ) as SimpleDateFormat
        sdf.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.timeZone = TimeZone.getTimeZone("IST")
        val date = sdf.parse(str)
        val dayOfTheWeek =
            android.text.format.DateFormat.format("EEEE", date) as String

        val day = android.text.format.DateFormat.format("dd", date) as String

        val monthString = android.text.format.DateFormat.format("MMM", date) as String

        val monthNumber = android.text.format.DateFormat.format("MM", date) as String

        val year = android.text.format.DateFormat.format("yyyy", date) as String

        return "$monthString $day, $year"

    }

}