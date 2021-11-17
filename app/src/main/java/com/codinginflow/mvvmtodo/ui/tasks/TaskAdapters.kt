package com.codinginflow.mvvmtodo.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.databinding.ItemTaskBinding

class TaskAdapters(private val listner: onItemClickListner):ListAdapter<Task, TaskAdapters.TaskViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem=getItem(position)
        holder.bind(currentItem)
    }

   inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.apply {
                root.setOnClickListener {
                    val position =adapterPosition
                    if (position != RecyclerView.NO_POSITION ){
                        val task = getItem(position)
                        listner.onItemClick(task)
                    }
                }
                checkBoxCompleted.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val task =getItem(position)
                        listner.onCheckBokClicked(task,checkBoxCompleted.isChecked)
                    }
                }
            }
        }

       fun bind(task: Task){
           binding.apply {
               checkBoxCompleted.isChecked = task.completed
               tvName.text=task.name
               tvName.paint.isStrikeThruText=task.completed
               labelPriority.isVisible=task.important
           }
       }
   }

    interface  onItemClickListner{
    fun onItemClick(task: Task)
    fun onCheckBokClicked(task: Task, isChecked : Boolean )
    }

    class DiffCallback :DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task)= oldItem == newItem

    }
}