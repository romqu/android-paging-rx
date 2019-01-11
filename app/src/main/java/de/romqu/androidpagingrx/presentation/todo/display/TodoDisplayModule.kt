package de.romqu.androidpagingrx.presentation.todo.display

import dagger.Module
import dagger.Provides
import de.romqu.androidpagingrx.presentation.todo.TodoScope
import io.reactivex.subjects.PublishSubject

@Module
object TodoDisplayModule {

    @Provides
    @JvmStatic
    @TodoScope
    fun provideOnLoadInitialEventSubject()
            : PublishSubject<TodoDisplayView.Event.OnLoadInitial> =
        PublishSubject.create()

    @Provides
    @JvmStatic
    @TodoScope
    fun provideOnLoadAfterEventSubject()
            : PublishSubject<TodoDisplayView.Event.OnLoadAfter> =
        PublishSubject.create()
}