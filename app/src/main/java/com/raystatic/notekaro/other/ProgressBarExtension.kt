package com.raystatic.notekaro.other

import android.opengl.Visibility
import android.view.View
import android.widget.ProgressBar

object ViewExtension {

    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.hide(){
        this.visibility = View.GONE
    }


}