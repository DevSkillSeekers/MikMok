package com.example.android.mikmok.data

import android.util.Log
import com.example.android.mikmok.data.model.MikMokResponse

class DataManager {
    private var _dataList: MikMokResponse? = null

    val dataList: MikMokResponse?
        get() = _dataList

    fun saveMikMokData(data: MikMokResponse) {
        _dataList = data
        Log.i(TAG, "data saved: $_dataList")

    }

    companion object {
        const val TAG = "DataManager"
    }
}