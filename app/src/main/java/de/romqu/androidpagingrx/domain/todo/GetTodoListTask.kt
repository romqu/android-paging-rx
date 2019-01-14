package de.romqu.androidpagingrx.domain.todo

import de.romqu.androidpagingrx.core.Result
import de.romqu.androidpagingrx.data.TodoEntity
import de.romqu.androidpagingrx.data.TodoRepo
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTodoListTask @Inject constructor(
    private val repo: TodoRepo
){

    operator fun invoke(key: Long, limit: Long): Single<Result<List<TodoEntity>>> =
        repo.getAll(key, limit)
}