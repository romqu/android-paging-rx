package de.romqu.androidpagingrx.presentation.todo.display

import androidx.paging.ItemKeyedDataSource
import de.romqu.androidpagingrx.data.TodoEntity
import io.reactivex.subjects.PublishSubject

class TodoItemKeyedDataSource(
    private val onLoadInitialEventSubject: PublishSubject<
            TodoDisplayView.Event.OnLoadInitial>,
    private val onLoadAfterEventSubject: PublishSubject<
            TodoDisplayView.Event.OnLoadAfter>
) : ItemKeyedDataSource<Long, TodoEntity>() {

    override fun getKey(item: TodoEntity) = item.id.id

    override fun loadBefore(
        params: LoadParams<Long>,
        callback: LoadCallback<TodoEntity>
    ) {
    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<TodoEntity>
    ) =
        onLoadInitialEventSubject.onNext(
            TodoDisplayView
                .Event
                .OnLoadInitial(
                    params,
                    callback
                )
        )


    override fun loadAfter(
        params: LoadParams<Long>,
        callback: LoadCallback<TodoEntity>
    ) =

        onLoadAfterEventSubject.onNext(
            TodoDisplayView
                .Event
                .OnLoadAfter(
                    params,
                    callback
                )
        )

}