package com.example.appdi.viewmodel

import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HistoricalViewModel: ViewModel(){

    val historical=mutableStateListOf<String>()

    fun register(message:String){
        historical.add(message)
    }

    var likes by mutableIntStateOf(0)
    private set

    fun addLikes(){
        likes++
    }



}