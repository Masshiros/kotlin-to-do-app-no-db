package com.example.todoapp1.fragments.list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp1.data.models.EPriority
import com.example.todoapp1.data.models.ToDoEntity
import com.example.todoapp.fragments.list.ListFragmentDirections

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    var onItemCheckedListener: ((ToDoEntity, Boolean) -> Unit)? = null
    var datas = mutableListOf<ToDoEntity>()
    class MyViewHolder(view:View): RecyclerView.ViewHolder(view)  { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = datas[position]
        holder.itemView.findViewById<TextView>(R.id.title_txt).text = currentItem.title
        holder.itemView.findViewById<TextView>(R.id.description_txt).text = currentItem.description
        holder.itemView.findViewById<ConstraintLayout>(R.id.row_background).setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
        when(currentItem.priority){
            EPriority.HIGH -> holder.itemView.findViewById<CardView>(R.id.priority_indicator).setCardBackgroundColor(ContextCompat.getColor(
                holder.itemView.context,
                R.color.red))
            EPriority.MEDIUM -> holder.itemView.findViewById<CardView>(R.id.priority_indicator).setCardBackgroundColor(ContextCompat.getColor(
                holder.itemView.context,
                R.color.yellow))
            EPriority.LOW -> holder.itemView.findViewById<CardView>(R.id.priority_indicator).setCardBackgroundColor(ContextCompat.getColor(
                holder.itemView.context,
                R.color.green))

        }
        holder.itemView.findViewById<CheckBox>(R.id.completed_checkbox).apply {
            isChecked = currentItem.completed
            setOnCheckedChangeListener(null)
            setOnCheckedChangeListener{_,isChecked ->
                onItemCheckedListener?.invoke(currentItem,isChecked)
            }
        }
    }
    fun setData(toDoData: List<ToDoEntity>){
        val diffUtil = ToDoDiffUtil(datas,toDoData)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.datas.clear()
        this.datas.addAll(toDoData)

        diffResult.dispatchUpdatesTo(this)
        Log.d("ListAdapter","Datas: ${this.datas.size}")
    }

}
