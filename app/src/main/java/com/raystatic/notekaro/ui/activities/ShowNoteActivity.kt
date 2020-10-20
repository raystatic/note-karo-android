package com.raystatic.notekaro.ui.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.raystatic.notekaro.R
import com.raystatic.notekaro.data.local.notes.Note
import com.raystatic.notekaro.data.requests.DeleteNoteRequest
import com.raystatic.notekaro.other.Constants
import com.raystatic.notekaro.other.PrefManager
import com.raystatic.notekaro.other.Status
import com.raystatic.notekaro.other.Utility
import com.raystatic.notekaro.other.ViewExtension.hide
import com.raystatic.notekaro.other.ViewExtension.show
import com.raystatic.notekaro.ui.viewmodels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_show_note.*
import kotlinx.android.synthetic.main.activity_show_note.imgBack
import kotlinx.android.synthetic.main.activity_show_note.tvNoteTitle
import kotlinx.android.synthetic.main.delete_confirmation_layout.view.*
import javax.inject.Inject

@AndroidEntryPoint
class ShowNoteActivity : AppCompatActivity() {

    @Inject
    lateinit var prefManager: PrefManager

    private val vm:NotesViewModel by viewModels()

    private lateinit var deleteDialogView: View

    private lateinit var deleteDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_note)

        val note = intent.getParcelableExtra<Note>("note_data")

        if (note == null) {
            finish()
        }

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
            showDeleteDialog(note)
        }

        subscribeToObservers(note)

    }

    private fun subscribeToObservers(note: Note?) {

        vm.deleteNotesResponse.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    it.data?.let {res->
                        if (res.success){
                            vm.deleteNoteByIdFromLocal(note?._id.toString())
                            finish()
                        }else{
                            Utility.showToast(res.message.toString(),this)
                        }
                        if (deleteDialog.isShowing){
                            deleteDialogView.deleteProgress.hide()
                            deleteDialogView.btnCancel.isEnabled = true
                            deleteDialogView.btnDelete.isEnabled = true
                        }
                    }
                }
                Status.LOADING -> {
                    if (deleteDialog.isShowing){
                        deleteDialogView.deleteProgress.show()
                        deleteDialogView.btnCancel.isEnabled = false
                        deleteDialogView.btnDelete.isEnabled = false
                    }
                }
                Status.ERROR -> {
                    if (deleteDialog.isShowing){
                        deleteDialogView.deleteProgress.hide()
                        deleteDialogView.btnCancel.isEnabled = true
                        deleteDialogView.btnDelete.isEnabled = true
                    }

                    Utility.showToast(it.message.toString(),this)

                }
            }
        })

    }

    private fun showDeleteDialog(note: Note?) {
        deleteDialog = Dialog(this)
        deleteDialogView = LayoutInflater.from(this).inflate(R.layout.delete_confirmation_layout, null)

        deleteDialog.apply {
            setContentView(deleteDialogView)
            setCancelable(true)
            deleteDialogView.btnCancel.setOnClickListener {
                this.cancel()
            }

            deleteDialogView.btnDelete.setOnClickListener {
                val deleteNoteRequest = DeleteNoteRequest(note?._id.toString())
                vm.deleteNote(prefManager.getString(Constants.JWT_TOKEN).toString(),deleteNoteRequest)
            }
        }

        deleteDialog.show()

        val window = deleteDialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

    }

}