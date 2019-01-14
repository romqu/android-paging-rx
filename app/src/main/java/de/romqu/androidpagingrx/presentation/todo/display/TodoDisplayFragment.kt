package de.romqu.androidpagingrx.presentation.todo.display

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import de.romqu.androidpagingrx.R
import de.romqu.androidpagingrx.appComponent
import de.romqu.androidpagingrx.common.extension.toObservable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_todo_display.*
import javax.inject.Inject

class TodoDisplayFragment : Fragment() {

    companion object {
        fun newInstance(): TodoDisplayFragment = TodoDisplayFragment()
    }

    @Inject
    lateinit var viewModel: TodoDisplayViewModel

    @Inject
    lateinit var todoDisplayAdapter: TodoDisplayAdapter

    @Inject
    lateinit var onLoadInitialEventSubject
            : PublishSubject<TodoDisplayView.Event.OnLoadInitial>

    @Inject
    lateinit var onLoadAfterEventSubject
            : PublishSubject<TodoDisplayView.Event.OnLoadAfter>

    @Inject
    lateinit var onInitEventSubject:
            PublishSubject<TodoDisplayView.Event.OnInit>

    private val subs = CompositeDisposable()

    private val component by lazy(LazyThreadSafetyMode.NONE) {

        appComponent()
            .todoComponentBuilder()
            .build()
    }

    override fun onAttach(context: Context) {

        component.inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(
            R.layout.fragment_todo_display,
            container,
            false
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setup()
    }

    override fun onDestroy() {
        super.onDestroy()

        subs.clear()
    }

    // -----------------------------------------------------------------------------------------

    private fun setup() {

        setupRecyclerView()

        setupEvents()
    }

    private fun setupRecyclerView() {
        todoDisplayRecyclerView.apply {
            adapter = todoDisplayAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupEvents() {

        Observable.mergeArray(
            onInitEventSubject.toObservable(),
            onLoadInitialEventSubject.toObservable(),
            onLoadAfterEventSubject.toObservable(),
            todoDisplaySwipeRefreshLayout
                .refreshes()
                .map {
                    TodoDisplayView
                        .Event
                        .OnRefresh(todoDisplayAdapter)
                })
            .observeOn(Schedulers.io())
            .compose(viewModel())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::render)
            .addTo(subs)

        onInitEventSubject.onNext(
            TodoDisplayView
                .Event
                .OnInit
        )
    }

    private fun render(viewState: TodoDisplayView.State) {
        return when (viewState.renderEvent) {

            TodoDisplayView.RenderEvent.Init ->
                init()

            is TodoDisplayView.RenderEvent.UpdateItemList ->
                updateItemList(viewState.renderEvent)


            TodoDisplayView.RenderEvent.StopRefresh ->
                stopRefreshEvent(viewState)


            TodoDisplayView.RenderEvent.None -> Unit

            is TodoDisplayView.RenderEvent.Error -> TODO()
        }

    }

    private fun init() {}

    private fun updateItemList(renderEvent: TodoDisplayView.RenderEvent.UpdateItemList) {

        todoDisplayAdapter.submitList(renderEvent.pagedList)
    }

    private fun stopRefreshEvent(viewState: TodoDisplayView.State) {

        todoDisplaySwipeRefreshLayout.isRefreshing = viewState.isRefreshing
    }
}
