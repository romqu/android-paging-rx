package de.romqu.androidpagingrx

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import de.romqu.androidpagingrx.core.di.AppComponent
import de.romqu.androidpagingrx.core.di.DaggerAppComponent

class TodoApplication : Application() {

    private val appComponent: AppComponent by lazy(LazyThreadSafetyMode.NONE) {
        DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        appComponent.inject(this)
    }

    companion object {
        @JvmStatic
        fun appComponent(context: Context) =
            (context.applicationContext as TodoApplication).appComponent
    }

}

fun Activity.appComponent() = TodoApplication.appComponent(this)
fun Fragment.appComponent() = TodoApplication.appComponent(requireContext())