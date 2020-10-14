package com.raystatic.notekaro.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.raystatic.notekaro.R
import com.raystatic.notekaro.data.requests.CreateNoteRequest
import com.raystatic.notekaro.other.Constants
import com.raystatic.notekaro.other.PrefManager
import com.raystatic.notekaro.other.Status
import com.raystatic.notekaro.other.Utility
import com.raystatic.notekaro.ui.viewmodels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.sasikanth.colorsheet.ColorSheet
import dev.sasikanth.colorsheet.utils.ColorSheetUtils
import kotlinx.android.synthetic.main.activity_create_note.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        imgBack.setOnClickListener {
            //show confirmation menu
        }

        val colors = resources.getIntArray(R.array.colors)

     //   selectedColor = savedInstanceState?.getInt(COLOR_SELECTED) ?: colors.first()

        btnSave.setOnClickListener {

            val title = etTitle.text.toString()
            val text = etText.text.toString()

            if (title.isEmpty()){
                Utility.showToast("Please provide a title", this)
                return@setOnClickListener
            }

            if (text.isEmpty()){
                Utility.showToast("Please type something in the note", this)
                return@setOnClickListener
            }

//            ColorSheet().cornerRadius(8)
//                .colorPicker(
//                    colors = colors,
//                    selectedColor = selectedColor,
//                    listener = {color->
//                        selectedColor = color
//                    }
//                ).show(supportFragmentManager)

            selectedColor = colors[Random.nextInt(0,6)]
            val themeColor = ColorSheetUtils.colorToHex(selectedColor)


            Timber.d("text length ${text.trim().length}")

//            title = when {
//                text.trim().length > 30 -> {
//                    text.substring(0..Random.nextInt(15,29))
//                }
//                else -> {
//                    text.substring(0..Random.nextInt(0, text.length -1))
//                }
//            }

            Timber.d("theme color choosen $themeColor")

            val createNoteRequest = CreateNoteRequest(title.trim(), text, themeColor)

            val token = prefManager.getString(Constants.JWT_TOKEN).toString()

            vm.createNewNote(token, createNoteRequest)
        }

        subscribeToObservers()

    }

    private fun subscribeToObservers() {

        vm.createdNote.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    it.data?.let {res->
                        Utility.showToast("Note created successfully",this)
                        res._note?.let { it1 ->
                            vm.addNoteToLocal(it1)
                            finish()
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Utility.showToast(it.message.toString(),this)
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