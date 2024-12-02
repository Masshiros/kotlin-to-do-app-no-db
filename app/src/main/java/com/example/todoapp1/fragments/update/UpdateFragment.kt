package com.example.todoapp1.fragments.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp1.data.SharedVM
import com.example.todoapp1.data.models.EPriority
import com.example.todoapp1.data.models.ToDoEntity
import com.example.todoapp1.data.viewmodels.ToDoVM


class UpdateFragment : Fragment() {
    private val args by navArgs<UpdateFragmentArgs >()
    private val mSharedVM: SharedVM by viewModels()
    private val mToDoVM: ToDoVM by viewModels()
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var prioritySpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)
        titleEditText = view.findViewById(R.id.current_title_at)
        descriptionEditText = view.findViewById(R.id.current_description_at)
        prioritySpinner = view.findViewById(R.id.current_priorities_spinner)
        setHasOptionsMenu(true)
        titleEditText.setText(args.currentItem.title)
        descriptionEditText.setText(args.currentItem.description)
        prioritySpinner.setSelection(mSharedVM.parsePriorityToInteger(args.currentItem.priority))
        prioritySpinner.onItemSelectedListener = mSharedVM.listener
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save->updateItem()
            R.id.menu_delete->confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }
    // delete confirm dialog
    private fun confirmItemRemoval() {
       val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_->
            mToDoVM.deleteItem(args.currentItem)
            Toast.makeText(requireContext(),"Successfully Removed: '${args.currentItem.title}'",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        builder.setNegativeButton("No"){_,_ ->}
        builder.setTitle("Delete ${args.currentItem.title}")
        builder.setMessage("Are you sure you want to remove '${args.currentItem.title}'?")
        builder.create().show()
    }

    private fun updateItem() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val priority = prioritySpinner.selectedItem.toString()
        val validation = mSharedVM.validateDataFromUser(title,description)
        if(validation){
            val updatedItem = ToDoEntity(
                args.currentItem.id,
                title,
                mSharedVM.mapPriority(priority),
                description
            )
            mToDoVM.updateData(updatedItem)
            Toast.makeText(requireContext(),"Successfully added", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        else{
            Toast.makeText(requireContext(),"Please fill out all fields",Toast.LENGTH_LONG).show()
        }
    }

}