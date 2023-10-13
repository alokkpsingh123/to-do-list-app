package com.codinginflow.mvvmtodo.ui.tasks

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.databinding.ItemTaskBinding


class TasksAdapter(private val context : Context, private val listener : OnItemClickListener): ListAdapter<Task, TasksAdapter.TaskViewHolder >(
    DiffCallback()
) {
    inner class TaskViewHolder(private val binding : ItemTaskBinding) : ViewHolder(binding.root){
        /*
          declare the listener inside the viewHolder because
          it is created once on other hand in onbindview
          is called every time when we scroll the screen
        */
        init {
          binding.apply {
              root.setOnClickListener{
                  val position = adapterPosition
                  if(position != RecyclerView.NO_POSITION){
                      val task = getItem(position)
                      listener.onItemClick(task)
                  }
              }
              checkBoxCompleted.setOnClickListener{
                  val position = adapterPosition
                  if(position != RecyclerView.NO_POSITION){
                      val task = getItem(position)
                      listener.onCheckBoxClick(task, checkBoxCompleted.isChecked)
                  }
              }
          }
        }

        fun bind(task : Task){
            binding.apply {
                val light_red = ContextCompat.getColor(context, R.color.light_red)
                val ligt_green = ContextCompat.getColor(context, R.color.light_green)
                checkBoxCompleted.isChecked = task.completed
                textviewName.text = task.name
                labelPriority.isVisible = task.important

                if(task.completed){
                    textviewName.paint.isStrikeThruText = task.completed
                    taskRoot.backgroundTintList = ColorStateList.valueOf(light_red)
                    textviewName.setTextColor(ColorStateList.valueOf(Color.WHITE))
                }else if(labelPriority.isVisible) {
                    taskRoot.backgroundTintList = ColorStateList.valueOf(ligt_green)
                    textviewName.setTextColor(ColorStateList.valueOf(Color.WHITE))
                }else if(task.completed && labelPriority.isVisible){
                    textviewName.paint.isStrikeThruText = task.completed
                    taskRoot.backgroundTintList = ColorStateList.valueOf(light_red)
                    textviewName.setTextColor(ColorStateList.valueOf(Color.WHITE))
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        task?.let {
            holder.bind(task)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(task: Task)
        fun onCheckBoxClick(task: Task, isChecked : Boolean)
    }
}

class DiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }

}