package com.example.appdavid3_4.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HistoricalViewModel: ViewModel() {
    val historical = mutableStateListOf<String>()

    var likes by mutableStateOf(0)
        private set

    fun registrar(message:String){
        historical.add(message)
    }

    fun addLikes(){
        likes++
    }
}