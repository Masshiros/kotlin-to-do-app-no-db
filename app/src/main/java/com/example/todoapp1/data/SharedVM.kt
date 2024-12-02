package com.example.todoapp1.data

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.R
import com.example.todoapp1.data.models.EPriority
import com.example.todoapp1.data.models.ToDoEntity

class SharedVM(application:Application): AndroidViewModel(application) {
    val emptyDb: MutableLiveData<Boolean> = MutableLiveData(false)
    fun checkEmptyDb(datas: List<ToDoEntity>){
        emptyDb.value = datas.isEmpty()
    }
    val listener: AdapterView.OnItemSelectedListener = object :
    AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
           when(pos){
               0 -> {
                   (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,
                       R.color.red))
               }
               1 -> {
                   (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,
                       R.color.yellow))
               }
               2 -> {
                   (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,
                       R.color.green))
               }
           }
        }
    }
    fun validateDataFromUser(title:String, description: String):Boolean{
        return !(title.isEmpty() || description.isEmpty())
    }
    fun mapPriority(priority: String): EPriority {
        return when(priority){
            "High Priority" -> {
                EPriority.HIGH}
            "Medium Priority" -> {
                EPriority.MEDIUM}
            "Low Priority" -> {
                EPriority.LOW}
            else -> EPriority.LOW
        }
    }
    fun parsePriorityToInteger(priority: EPriority):Int{
        return when(priority){
            EPriority.HIGH->0
            EPriority.MEDIUM->1
            EPriority.LOW->2
        }
    }

}