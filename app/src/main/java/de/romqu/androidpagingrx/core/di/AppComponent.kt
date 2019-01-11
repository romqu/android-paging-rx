package de.romqu.androidpagingrx.core.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import de.romqu.androidpagingrx.presentation.MainActivity
import de.romqu.androidpagingrx.presentation.todo.TodoComponent
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {

    fun inject(application: Application)
    fun inject(mainActivity: MainActivity)

    fun todoComponentBuilder(): TodoComponent.Builder

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}