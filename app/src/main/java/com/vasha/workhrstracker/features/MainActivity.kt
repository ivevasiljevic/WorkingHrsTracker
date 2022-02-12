package com.vasha.workhrstracker.features

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vasha.workhrstracker.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_WorkHrsTracker)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}