package com.bootcamp.bootcampmagic.utils

import android.text.Editable
import android.text.TextWatcher
import java.util.*

abstract class SearchListener: TextWatcher{
    private var timer: Timer? = null

    override fun afterTextChanged(s: Editable?) {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                onSearchChanged(s.toString())
            }
        }, 1200)
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        timer?.cancel()
    }

    abstract fun onSearchChanged(filter: String)
}