package com.raystatic.notekaro.ui.activities

import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.raystatic.notekaro.R
import com.raystatic.notekaro.data.local.notes.Note
import com.raystatic.notekaro.other.PrefManager
import com.raystatic.notekaro.other.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_create_note.*
import kotlinx.android.synthetic.main.activity_show_note.*
import kotlinx.android.synthetic.main.activity_show_note.imgBack
import kotlinx.android.synthetic.main.activity_show_note.tvNoteTitle
import kotlinx.android.synthetic.main.delete_confirmation_layout.view.*
import kotlinx.android.synthetic.main.title_dialog.view.*
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

        imgDelete.setOnClickListener {
            showDeleteDialog()
        }

    }

    private fun showDeleteDialog() {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.delete_confirmation_layout, null)

        dialog.apply {
            setContentView(view)
            setCancelable(true)
            view.btnCancel.setOnClickListener {
                this.cancel()
            }

            view.btnDelete.setOnClickListener {
                
            }
        }

        dialog.show()

        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

    }

}