package com.example.todoapp1.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun hideVirtualKeyBoard(act:Activity){
    val inputMM = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val focusedView = act.currentFocus
    focusedView?.let{
        inputMM.hideSoftInputFromWindow(
            focusedView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}
fun<T> LiveData<T>.oneTimeObserve(lifecycleOwner: LifecycleOwner,observer: Observer<T>){
    observe(lifecycleOwner,object:Observer<T>{
        override fun onChanged(value: T) {
            observer.onChanged(value)
            removeObserver(this)
        }
    })
}