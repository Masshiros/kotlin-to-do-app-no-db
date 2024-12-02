package com.example.todoapp1.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Entity(tableName="todos_table")
@Parcelize
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    var title: String,
    var priority: EPriority,
    var description: String,
    var completed: Boolean=false
): Parcelable
