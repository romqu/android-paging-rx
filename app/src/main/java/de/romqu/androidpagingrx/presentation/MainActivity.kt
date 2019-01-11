package de.romqu.androidpagingrx.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.romqu.androidpagingrx.R
import de.romqu.androidpagingrx.appComponent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        appComponent().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
