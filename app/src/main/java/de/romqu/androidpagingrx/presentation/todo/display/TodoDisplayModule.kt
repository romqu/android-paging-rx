package de.romqu.androidpagingrx.presentation.todo.display

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import dagger.Module
import dagger.Provides
import de.romqu.androidpagingrx.common.Constants
import de.romqu.androidpagingrx.data.TodoEntity
import de.romqu.androidpagingrx.presentation.todo.TodoScope
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

@Module
object TodoDisplayModule {

    @Provides
    @JvmStatic
    @TodoScope
    fun provideViewState(): TodoDisplayView.State =
        TodoDisplayView.State()

    @Provides
    @JvmStatic
    @TodoScope
    fun provideOnInitEventSubject()
            : PublishSubject<TodoDisplayView.Event.OnInit> =
        PublishSubject.create()

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



    @Provides
    @JvmStatic
    @TodoScope
    fun providePageListConfig(): PagedList.Config = PagedList
        .Config
        .Builder()
        .apply {
            setEnablePlaceholders(true)
            setInitialLoadSizeHint(Constants.Pagination.INITIAL_LOAD_SIZE)
            setPageSize(Constants.Pagination.PAGE_SIZE)
        }.build()

    @Provides
    @JvmStatic
    @TodoScope
    fun provideObservablePagedList(todoDisplayItemKeyedDataFactory: TodoDisplayItemKeyedDataFactory,
                                 pagedListConfig: PagedList.Config)
            : Observable<PagedList<TodoEntity>> =

        RxPagedListBuilder(todoDisplayItemKeyedDataFactory, pagedListConfig)
            .buildObservable()
}