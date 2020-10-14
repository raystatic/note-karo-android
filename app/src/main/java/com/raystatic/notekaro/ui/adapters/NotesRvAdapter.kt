package com.raystatic.notekaro.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raystatic.notekaro.R
import com.raystatic.notekaro.data.local.notes.Note
import com.raystatic.notekaro.other.Utility
import kotlinx.android.synthetic.main.note_item.view.*

class NotesRvAdapter(var listener:NotesListener) : RecyclerView.Adapter<NotesRvAdapter.NotesViewHolder>(){

    private var data:List<Note>?=null

    interface NotesListener{
        fun onNoteClicked(note: Note)
    }

    fun setData(list: List<Note>){
        data = list
        notifyDataSetChanged()
    }

    inner class NotesViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder  =
        NotesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.note_item, parent, false)
        )

    override fun getItemCount(): Int  = data?.size ?: 0

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = data?.get(position)

        holder.itemView.apply {
            if (note?.color.isNullOrEmpty()){
                linNoteBackground.setBackgroundColor(Color.parseColor("#9585BA"))
            }else{
                linNoteBackground.setBackgroundColor(Color.parseColor(note?.color.toString()))
            }

            tvNoteTitle.text = note?.title

            tvDate.text = Utility.getFormattedDateFromISO(note?.updatedAt.toString())

            setOnClickListener {
                note?.let { it1 ->
                    listener.onNoteClicked(it1)
                }
            }

        }

    }
}