package com.raystatic.notekaro.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.RequestManager
import com.raystatic.notekaro.R
import com.raystatic.notekaro.data.local.notes.Note
import com.raystatic.notekaro.other.Constants
import com.raystatic.notekaro.other.PrefManager
import com.raystatic.notekaro.other.Status
import com.raystatic.notekaro.other.Utility
import com.raystatic.notekaro.other.ViewExtension.hide
import com.raystatic.notekaro.other.ViewExtension.show
import com.raystatic.notekaro.ui.adapters.NotesRvAdapter
import com.raystatic.notekaro.ui.viewmodels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), NotesRvAdapter.NotesListener {

    @Inject
    lateinit var prefManager:PrefManager

    @Inject
    lateinit var requestManager:RequestManager

    private val vm: NotesViewModel by viewModels()

    private lateinit var notesRvAdapter:NotesRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Timber.d("jwt token: ${prefManager.getString(Constants.JWT_TOKEN)}")

        val token = prefManager.getString(Constants.JWT_TOKEN).toString()
        val avatar = prefManager.getString(Constants.USER_AVATAR).toString()

        requestManager.apply {
            load(avatar)
                .placeholder(R.drawable.ic_baseline_person_24)
                .error(R.drawable.ic_baseline_person_24)
                .into(imgAvatar)
        }


        notesRvAdapter = NotesRvAdapter(this)
        rvNotes.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        rvNotes.adapter = notesRvAdapter

        fabNewNote.setOnClickListener {
            startActivity(Intent(this,CreateNoteActivity::class.java))
        }

        swipeRefresh.setOnRefreshListener {
            vm.getAllNotes(token)
            swipeRefresh.isRefreshing = false
        }

        vm.getAllNotes(token)

        progress_home.show()

        subscribeToObservers()

    }

    override fun onNoteClicked(note: Note) {
        val intent = Intent(this, ShowNoteActivity::class.java)
        intent.putExtra("note_data", note)
        startActivity(intent)
    }

    private fun subscribeToObservers() {

        vm.currentNotes.observe(this, Observer {
            it?.let {
                notesRvAdapter.setData(it)
            }

        })

        vm.notes.observe(this, Observer {
            when(it.status){

                Status.SUCCESS -> {
                    it.data?.let {res->
                        val notes = res._notes

                        progress_home.hide()

                        if (notes.isNullOrEmpty()){
                            linMessage.show()
                            tvHomeMessage.text = "No notes found. Please create one"
                            rvNotes.hide()

                        }else{
                            linMessage.hide()
                            rvNotes.show()
                            vm.deleteAllNotesFromLocal()
                            notes.forEach {n->
                                vm.addNoteToLocal(n)
                            }
                        }

                    }
                }
                Status.ERROR -> {
                    progress_home.hide()
                    Utility.showToast(it.message.toString(), this)
                }
                Status.LOADING -> {
                    progress_home.show()
                }

            }
        })

    }
}