package com.raystatic.notekaro.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.raystatic.notekaro.R
import com.raystatic.notekaro.data.local.notes.Note
import com.raystatic.notekaro.data.requests.CreateNoteRequest
import com.raystatic.notekaro.data.requests.UpdateNoteRequest
import com.raystatic.notekaro.other.Constants
import com.raystatic.notekaro.other.PrefManager
import com.raystatic.notekaro.other.Status
import com.raystatic.notekaro.other.Utility
import com.raystatic.notekaro.other.ViewExtension.hide
import com.raystatic.notekaro.other.ViewExtension.show
import com.raystatic.notekaro.ui.viewmodels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.sasikanth.colorsheet.ColorSheet
import dev.sasikanth.colorsheet.utils.ColorSheetUtils
import kotlinx.android.synthetic.main.activity_create_note.*
import kotlinx.android.synthetic.main.activity_create_note.imgBack
import kotlinx.android.synthetic.main.activity_create_note.tvNoteTitle
import kotlinx.android.synthetic.main.activity_show_note.*
import kotlinx.android.synthetic.main.title_dialog.view.*
import timber.log.Timber
import java.lang.Math.random
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class CreateNoteActivity : AppCompatActivity() {

    private val vm:NotesViewModel by viewModels()

    @Inject
    lateinit var prefManager: PrefManager

    private var selectedColor: Int = ColorSheet.NO_COLOR

    private var isUpdatable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        val note = intent.getParcelableExtra<Note>("note_data")

        note?.let {
            tvNoteTitle.text = it.title
            etText.setText(it.text)
            isUpdatable = true
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

        val colors = resources.getIntArray(R.array.colors)


        tvNoteTitle.setOnClickListener {
            showTitleDialog()
        }

        btnSave.setOnClickListener {

            val title = tvNoteTitle.text.toString()
            val text = etText.text.toString()

            if (title.isEmpty()){
                Utility.showToast("Please provide a title", this)
                return@setOnClickListener
            }

            if (text.isEmpty()){
                Utility.showToast("Please type something in the note", this)
                return@setOnClickListener
            }

            if (title == "Untitled"){
                showTitleDialog()
                return@setOnClickListener
            }

            val token = prefManager.getString(Constants.JWT_TOKEN).toString()

            if (isUpdatable){

                val updateNoteRequest = UpdateNoteRequest(title,text, note?.color.toString(), note?._id.toString())

                vm.updateExistingNote(token, updateNoteRequest)
            }else{
                selectedColor = colors[Random.nextInt(0,6)]
                val themeColor = ColorSheetUtils.colorToHex(selectedColor)

                Timber.d("theme color choosen $themeColor")

                val createNoteRequest = CreateNoteRequest(title.trim(), text, themeColor)

                vm.createNewNote(token, createNoteRequest)
            }

            savingProgress.show()
        }

        subscribeToObservers()

    }

    private fun showTitleDialog() {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.title_dialog, null)

        dialog.apply {
            setContentView(view)
            setCancelable(true)
            view.etTitle.hint = this@CreateNoteActivity.tvNoteTitle.text.toString()
            view.btnSave.setOnClickListener {
                val title = view.etTitle.text.toString()
                if (title.isEmpty()){
                    Utility.showToast("Please provide a title", this@CreateNoteActivity)
                    return@setOnClickListener
                }
                this@CreateNoteActivity.tvNoteTitle.text = title
                this.cancel()
            }
        }

        dialog.show()

        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

    }

    private fun subscribeToObservers() {

        vm.updateNoteResponse.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    it.data?.let {res->
                        res._note?.let { it1 ->
                            vm.updateNoteToLocal(it1)
                            savingProgress.hide()
                            finish()
                        }
                    }
                }
                Status.LOADING -> {
                    Utility.showToast("Updating Note", this)
                    savingProgress.show()
                }
                Status.ERROR -> {
                    Utility.showToast(it.message.toString(),this)
                    savingProgress.hide()
                }
            }
        })

        vm.createdNote.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    it.data?.let {res->
                        Utility.showToast("Note created successfully",this)
                        res._note?.let { it1 ->
                            vm.addNoteToLocal(it1)
                            savingProgress.hide()
                            finish()
                        }
                    }
                }
                Status.LOADING -> {
                    savingProgress.show()
                    Utility.showToast("Saving your note",this)
                }
                Status.ERROR -> {
                    Utility.showToast(it.message.toString(),this)
                    savingProgress.hide()
                }
            }
        })

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(COLOR_SELECTED, selectedColor)
    }

    companion object {
        private const val COLOR_SELECTED = "selectedColor"
    }
}