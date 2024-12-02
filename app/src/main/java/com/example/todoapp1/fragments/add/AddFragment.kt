package com.example.todoapp1.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp1.data.SharedVM
import com.example.todoapp1.data.models.EPriority
import com.example.todoapp1.data.models.ToDoEntity
import com.example.todoapp1.data.viewmodels.ToDoVM



class AddFragment : Fragment() {
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var prioritySpinner: Spinner

    private val mToDoVM: ToDoVM by viewModels()
    private val mSharedVM: SharedVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add, container, false)
        titleEditText = view.findViewById(R.id.title_at)
        descriptionEditText = view.findViewById(R.id.description_at)
        prioritySpinner = view.findViewById(R.id.priorities_spinner)

        prioritySpinner.onItemSelectedListener = mSharedVM.listener
        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add){
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val priority = prioritySpinner.selectedItem.toString()

        val validation = mSharedVM.validateDataFromUser(title,description)
        if(validation){
            val data = ToDoEntity(
                title=title,
                priority =  mSharedVM.mapPriority(priority),
                description=description,
            )
            mToDoVM.insertData(data)
            Toast.makeText(requireContext(),"Successfully added",Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
        else{
            Toast.makeText(requireContext(),"Please fill out all fields",Toast.LENGTH_LONG).show()
        }



    }


}