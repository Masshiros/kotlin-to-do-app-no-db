package com.example.todoapp1.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoapp1.data.models.ToDoEntity

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todos_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<ToDoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoEntity):Long

    @Update
    suspend fun updateData(toDoData: ToDoEntity)

    @Delete
    suspend fun deleteItem(toDoData: ToDoEntity)

    @Query("DELETE FROM todos_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM todos_table WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<ToDoEntity>>

    @Query("SELECT * FROM todos_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<ToDoEntity>>

    @Query("SELECT * FROM todos_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<ToDoEntity>>
    @Query("UPDATE todos_table SET completed = :completed WHERE id = :id")
    suspend fun markAsCompleted(id: Int,completed:Boolean)

    @Query("UPDATE todos_table SET completed = 1")
    suspend fun markAllAsCompleted()
}