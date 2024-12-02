package com.example.todoapp1.fragments.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todoapp.R
import com.example.todoapp1.data.SharedVM
import com.example.todoapp1.data.models.ToDoEntity
import com.example.todoapp1.data.viewmodels.ToDoVM
import com.example.todoapp1.fragments.list.adapter.ListAdapter
import com.example.todoapp1.utils.hideVirtualKeyBoard
import com.example.todoapp1.utils.oneTimeObserve
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class ListFragment : Fragment(), SearchView.OnQueryTextListener{
    private val adapter: ListAdapter by lazy { ListAdapter() }

    private val mToDoVM: ToDoVM by viewModels()
    private val mSharedVM: SharedVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        // fetch data with adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply{
            addDuration=300
        }
        // swipe to delete
        swipeToDelete(recyclerView)

        mToDoVM.getAllData.observe(viewLifecycleOwner, Observer { data ->
            mSharedVM.checkEmptyDb(data)
            // mark as complete
            adapter.onItemCheckedListener = {todoEntity,isChecked ->
                mToDoVM.markAsCompleted(todoEntity.id,isChecked)
                Toast.makeText(requireContext(),"Mark as completed successfully: '${todoEntity.title}'",Toast.LENGTH_SHORT).show()
            }
            adapter.setData(data)

            Log.d("ListFragment", "Received new data: ${data.size}")
        })
        mSharedVM.emptyDb.observe(viewLifecycleOwner, Observer{
            showEmptyDbViews(it)
        })

        // navigate
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)

        }
        view.findViewById<ConstraintLayout>(R.id.listLayout).setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }
        // menu
        setHasOptionsMenu(true)
        // hide keyboard
        hideVirtualKeyBoard(requireActivity())

        return view
    }
    private fun markAllAsCompleted(){
        mToDoVM.markAllAsCompleted()
        Toast.makeText(requireContext(),"Mark all as completed successfully",Toast.LENGTH_SHORT).show()

    }

    private fun showEmptyDbViews(emptyDb: Boolean) {
        if(emptyDb){
            view?.findViewById<TextView>(R.id.no_data_textView)?.visibility = View.VISIBLE
            view?.findViewById<ImageView>(R.id.no_data_imageView)?.visibility = View.VISIBLE

        }else{
            view?.findViewById<TextView>(R.id.no_data_textView)?.visibility = View.INVISIBLE
            view?.findViewById<ImageView>(R.id.no_data_imageView)?.visibility = View.INVISIBLE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu,menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }
    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallBack = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.datas[viewHolder.adapterPosition]
                mToDoVM.deleteItem(itemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                Toast.makeText(requireContext(),"Successfully remove: '${itemToDelete.title}'",Toast.LENGTH_SHORT).show()
                // restore data
                restoreDeletedData(viewHolder.itemView,itemToDelete)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    private fun restoreDeletedData(view:View,deletedData: ToDoEntity){
        val snackbar = Snackbar.make(
            view,"Deleted '${deletedData.title}'",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo"){
            mToDoVM.insertData(deletedData)
        }
        snackbar.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> mToDoVM.sortByHighPriority.observe(viewLifecycleOwner, Observer { adapter.setData(it) })
            R.id.menu_priority_low -> mToDoVM.sortByLowPriority.observe(viewLifecycleOwner, Observer { adapter.setData(it) })
            R.id.menu_mark_all -> markAllAsCompleted()
        }
        return super.onOptionsItemSelected(item)
    }
    // confirm delete all item
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_->
            mToDoVM.deleteAll()
            Toast.makeText(requireContext(),"Successfully Removed All Items",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ ->}
        builder.setTitle("Delete all")
        builder.setMessage("Are you sure you want to remove all items?")
        builder.create().show()
    }

    override fun onQueryTextSubmit(q: String?): Boolean {
        if(q!=null){
            searchThroughDb(q)
        }
        return true
    }

    private fun searchThroughDb(q: String) {
        val searchQuery = "%$q%"
        mToDoVM.searchDatabase(searchQuery).oneTimeObserve(viewLifecycleOwner, Observer {
            list -> list?.let{
                adapter.setData(it)
        }
        })
    }

    override fun onQueryTextChange(q: String?): Boolean {
        if(q!=null){
            searchThroughDb(q)
        }
        return true
    }

}