package de.romqu.androidpagingrx.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.romqu.androidpagingrx.R
import de.romqu.androidpagingrx.appComponent
import de.romqu.androidpagingrx.presentation.todo.display.TodoDisplayFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        appComponent().inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val fragment = TodoDisplayFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer,
                fragment,
                TodoDisplayFragment::class.java.simpleName)
            .show(fragment)
            .commit()


    }
}
