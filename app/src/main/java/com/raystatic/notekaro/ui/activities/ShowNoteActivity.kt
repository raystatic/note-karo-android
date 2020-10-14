package com.raystatic.notekaro.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.raystatic.notekaro.R
import com.raystatic.notekaro.data.local.notes.Note
import com.raystatic.notekaro.other.PrefManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_show_note.*
import javax.inject.Inject

@AndroidEntryPoint
class ShowNoteActivity : AppCompatActivity() {

    @Inject
    lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_note)

        val note = intent.getParcelableExtra<Note>("note_data")

        note?.let { n->
            tvNoteTitle.text = n.title
            tvNoteText.text=  n.text
        }

        tvNoteTitle.apply {
            ellipsize = TextUtils.TruncateAt.MARQUEE
            setSingleLine()
            marqueeRepeatLimit = 10
            isFocusable = true
            setHorizontallyScrolling(true)
            isFocusableInTouchMode = true
            requestFocus()
        }

        imgBack.setOnClickListener {
            finish()
        }

        imgEdit.setOnClickListener {
            val intent = Intent(this, CreateNoteActivity::class.java)
            intent.putExtra("note_data",note)
            startActivity(intent)
            finish()
        }

    }
}