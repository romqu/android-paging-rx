package de.romqu.androidpagingrx.presentation.todo.display

import androidx.paging.DataSource
import de.romqu.androidpagingrx.data.TodoEntity
import de.romqu.androidpagingrx.presentation.todo.TodoScope
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@TodoScope
class TodoDisplayItemKeyedDataFactory @Inject constructor(
    private val onLoadInitialEventSubject: PublishSubject<
            TodoDisplayView.Event.OnLoadInitial>,
    private val onLoadAfterEventSubject: PublishSubject<
            TodoDisplayView.Event.OnLoadAfter>
) : DataSource.Factory<Long, TodoEntity>() {


    override fun create(): DataSource<Long, TodoEntity> {
        return TodoItemKeyedDataSource(
            onLoadInitialEventSubject,
            onLoadAfterEventSubject)
    }
}