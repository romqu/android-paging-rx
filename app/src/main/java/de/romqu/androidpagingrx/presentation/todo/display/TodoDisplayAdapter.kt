package de.romqu.androidpagingrx.presentation.todo.display

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import de.romqu.androidpagingrx.R
import de.romqu.androidpagingrx.data.TodoEntity
import kotlinx.android.synthetic.main.item_todo_display.view.*
import javax.inject.Inject

class TodoDisplayAdapter @Inject constructor(
) : PagedListAdapter<TodoEntity, ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_todo_display,
                parent,
                false
            )

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position) ?: return

        holder.bind(item)
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(todoEntity: TodoEntity) {

        with(todoEntity) {
            itemView.textViewJobAdList.text = text
        }
    }
}

internal val DIFF_CALLBACK: DiffUtil.ItemCallback<TodoEntity> =
    object : DiffUtil.ItemCallback<TodoEntity>() {
        override fun areItemsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
            return oldItem.text == newItem.text
        }
    }