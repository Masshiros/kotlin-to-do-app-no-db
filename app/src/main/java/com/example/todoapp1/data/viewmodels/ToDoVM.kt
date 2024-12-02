package com.example.todoapp1.data.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp1.data.ToDoDatabase
import com.example.todoapp1.data.models.ToDoEntity
import com.example.todoapp1.data.repository.ToDoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoVM(application: Application): AndroidViewModel(application) {
    private val toDoDao = ToDoDatabase.getDatabase(
        application
    ).toDoDao()
    private val repository: ToDoRepo = ToDoRepo(toDoDao)

    val getAllData: LiveData<List<ToDoEntity>> = repository.getAllData
    val sortByHighPriority: LiveData<List<ToDoEntity>> = repository.sortByHighPriority
    val sortByLowPriority: LiveData<List<ToDoEntity>> = repository.sortByLowPriority

    fun insertData(toDoData: ToDoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
               val newRowId =  repository.insertData(toDoData)
                Log.d("ToDoVM", "Data inserted successfully with ID ${newRowId}")
            } catch (e: Exception) {
                Log.e("ToDoVM", "Error inserting data", e)
            }
        }
    }

    fun updateData(toDoData: ToDoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: ToDoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoEntity>> {
        return repository.searchDatabase(searchQuery)
    }
    fun markAsCompleted(id: Int,completed:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markAsCompleted(id,completed)
        }
    }

    fun markAllAsCompleted() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markAllAsCompleted()
        }
    }
}