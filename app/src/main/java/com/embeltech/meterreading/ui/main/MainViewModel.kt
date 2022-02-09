package com.embeltech.meterreading.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.embeltech.meterreading.ui.BaseViewModel

class MainViewModel : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}