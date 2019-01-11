package de.romqu.androidpagingrx.presentation.todo.display

import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import de.romqu.androidpagingrx.data.TodoEntity

class TodoDisplayView {

    sealed class Event {

        object OnInit : Event()

        class OnLoadInitial(
            val params: ItemKeyedDataSource.LoadInitialParams<Long>,
            val callback: ItemKeyedDataSource.LoadInitialCallback<TodoEntity>
        ) : Event()

        class OnLoadAfter(
            val params: ItemKeyedDataSource.LoadParams<Long>,
            val callback: ItemKeyedDataSource.LoadCallback<TodoEntity>
        ) : Event()

        class OnRefresh : Event()

    }

    data class State(
        val isRefreshing: Boolean = false
    )

    sealed class RenderEvent {

        object Init : RenderEvent()

        object None : RenderEvent()

        object StopRefresh: RenderEvent()

        class UpdateItemList(
            val pagedList: PagedList<TodoEntity>
        ) : RenderEvent()

        class Error(val message: String) : RenderEvent()

    }
}