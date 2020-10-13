package com.raystatic.notekaro.other

import android.opengl.Visibility
import android.view.View
import android.widget.ProgressBar

object ProgressBarExtension {

    fun ProgressBar.show() {
        this.visibility = View.VISIBLE
    }

    fun ProgressBar.hide(){
        this.visibility = View.GONE
    }


}