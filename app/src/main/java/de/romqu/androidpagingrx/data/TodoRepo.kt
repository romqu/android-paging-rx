package de.romqu.androidpagingrx.data

import de.romqu.androidpagingrx.common.extension.asSingleFromCallable
import de.romqu.androidpagingrx.common.extension.toSingleResult
import de.romqu.androidpagingrx.core.Result
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepo @Inject constructor() {

    fun getAll(key: Long, limit: Long): Single<Result<List<TodoEntity>>> {

        Thread.sleep(2000)

        return LongRange(key, key + limit)
            .map { idLong: Long ->
                TodoEntity(
                    id = TodoId(idLong),
                    text = idLong.toString()
                )
            }.toSingleResult()
    }


}