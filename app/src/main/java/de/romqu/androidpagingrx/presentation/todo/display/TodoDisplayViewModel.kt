package de.romqu.androidpagingrx.presentation.todo.display

import android.util.Log
import androidx.paging.PagedList
import de.romqu.androidpagingrx.common.extension.asObservable
import de.romqu.androidpagingrx.core.Result
import de.romqu.androidpagingrx.data.TodoEntity
import de.romqu.androidpagingrx.domain.todo.GetTodoListTask
import de.romqu.androidpagingrx.presentation.todo.TodoScope
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

@TodoScope
class TodoDisplayViewModel @Inject constructor(
    private var viewState: TodoDisplayView.State,
    private val getTodoListTask: GetTodoListTask,
    private val observablePagedList: Observable<PagedList<TodoEntity>>
) {

    operator fun invoke(): ObservableTransformer<TodoDisplayView.Event,
            TodoDisplayView.State> = onEvent

    private val onEvent = ObservableTransformer<TodoDisplayView.Event,
            TodoDisplayView.State> { upstream: Observable<TodoDisplayView.Event> ->

        upstream.publish { shared: Observable<TodoDisplayView.Event> ->

            Observable
                .mergeArray(
                    shared.ofType(TodoDisplayView.Event.OnInit::class.java),
                    shared.ofType(TodoDisplayView.Event.OnLoadInitial::class.java),
                    shared.ofType(TodoDisplayView.Event.OnLoadAfter::class.java),
                    shared.ofType(TodoDisplayView.Event.OnRefresh::class.java)
                )
                .compose(eventToViewState)
        }
    }

    private val eventToViewState = ObservableTransformer<TodoDisplayView.Event,
            TodoDisplayView.State> { upstream: Observable<TodoDisplayView.Event> ->

        upstream.flatMap { event: TodoDisplayView.Event ->
            when (event) {

                TodoDisplayView.Event.OnInit ->
                    onInitEvent()

                is TodoDisplayView.Event.OnLoadInitial ->
                    onLoadInitialEvent(event)

                is TodoDisplayView.Event.OnLoadAfter ->
                    onLoadAfterEvent(event)

                is TodoDisplayView.Event.OnRefresh ->
                    onRefreshEvent(event)
            }
        }
    }

    private fun onInitEvent(): Observable<TodoDisplayView.State> =

        observablePagedList
            .map { todoEntityPagedList: PagedList<TodoEntity> ->

                viewState = viewState.copy(
                    renderEvent = TodoDisplayView
                        .RenderEvent
                        .UpdateItemList(todoEntityPagedList)
                )

                viewState
            }


    private fun onRefreshEvent(event: TodoDisplayView.Event.OnRefresh)
            : Observable<TodoDisplayView.State> {

        viewState = viewState.copy(
            isRefreshing = true,
            renderEvent = TodoDisplayView
                .RenderEvent
                .None
        )

        event.todoDisplayAdapter
            .currentList!!
            .dataSource
            .invalidate()

        return viewState.asObservable()
    }


    private fun onLoadInitialEvent(event: TodoDisplayView.Event.OnLoadInitial)
            : Observable<TodoDisplayView.State> =

        getTodoListTask(
            key = 0,
            limit = event.params.requestedLoadSize.toLong()
        ).map { result ->

            result.map { todoEntityList: List<TodoEntity> ->
                event.callback.onResult(todoEntityList)
            }

        }.flatMapObservable { _: Result<Unit> ->

            viewState = viewState.copy(
                renderEvent = TodoDisplayView
                    .RenderEvent
                    .None
            )

            val stateList =
                mutableListOf(viewState.copy())

            if (viewState.isRefreshing) {

                viewState = viewState.copy(
                    renderEvent = TodoDisplayView
                        .RenderEvent
                        .StopRefresh,
                    isRefreshing = false
                )

                stateList.add(viewState.copy())
            }

            Observable
                .fromIterable(stateList)
        }


    private fun onLoadAfterEvent(event: TodoDisplayView.Event.OnLoadAfter)
            : Observable<TodoDisplayView.State> =

        getTodoListTask(
            key = event.params.key,
            limit = event.params.requestedLoadSize.toLong()
        ).map { result ->

            Log.d("TAG", event.params.key.toString())

            result.map { todoEntityList: List<TodoEntity> ->
                event.callback.onResult(todoEntityList)
            }
        }.map { _: Result<Unit> ->

            viewState = viewState.copy(
                renderEvent = TodoDisplayView
                    .RenderEvent
                    .None
            )

            viewState
        }.toObservable()


}
