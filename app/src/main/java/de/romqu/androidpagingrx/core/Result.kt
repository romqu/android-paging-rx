package de.romqu.androidpagingrx.core

import de.romqu.androidpagingrx.common.extension.asSingleFromCallable
import de.romqu.androidpagingrx.common.extension.toResult
import io.reactivex.Observable
import io.reactivex.Single

sealed class Result<out T> {

    data class Failure(val error: String) : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()

/*    fun getOrThrow(): T = match(
            { error ->

                throw Error(error)
            },
            {
                it
            })*/

    val isFailure get() = this is Failure

    val isSuccess get() = this is Success

    fun <R> onSuccess(invoke: (T) -> Result<R>): Result<R> =
        when (this) {
            is Failure -> this
            is Success -> invoke(data)
        }

    fun <R> match(onSuccess: (T) -> R, onFailure: (error: String) -> R): R =
        when (this) {
            is Success -> onSuccess(data)
            is Failure -> onFailure(error)
        }

    fun <R> flatMap(f: (T) -> Result<R>): Result<R> =
        match(f, { this as Failure })

    fun <R> map(f: (T) -> R): Result<R> =
        flatMap { Success(f(it)) }

    private fun <R> matchSingle(onFailure: (error: String) -> Single<R>, onSuccess: (T) -> Single<R>): Single<R> =
        when (this) {
            is Failure -> onFailure(error)
            is Success -> onSuccess(data)
        }

    fun <R> flatMapSingle(f: (T) -> Single<Result<R>>): Single<Result<R>> =
        matchSingle({ (this as Failure).asSingleFromCallable() }, f)

    fun <R> mapSingle(f: (T) -> R): Single<Result<R>> =
        flatMapSingle { success(f(it)).asSingleFromCallable() }

    private fun <R> matchObservable(onFailure: (error: String) -> Observable<R>, onSuccess: (T) -> Observable<R>): Observable<R> =
        when (this) {
            is Failure -> onFailure(error)
            is Success -> onSuccess(data)
        }

    fun <R> flatMapObservable(f: (T) -> Observable<Result<R>>): Observable<Result<R>> =
        matchObservable({ Observable.just(this as Failure) }, f)

    fun <R> mapObservable(f: (T) -> R): Observable<Result<R>> =
        flatMapObservable { Observable.just(success(f(it))) }


    companion object {

        fun <T> success(data: T) = Success(data) as Result<T>

        fun failure(data: String) = Failure(data) as Result<Nothing>

        fun failureSingle(data: String) = Single.fromCallable { failure(
            data
        ) as Result<String>
        }

        fun <T> unboxSuccessList(resultList: List<Result<T>>): Result<List<T>> {
            val resultSuccessList = resultList as List<Success<T>>

            return resultSuccessList
                .map { success ->
                    success.data
                }
                .toList()
                .toResult()
        }

        fun <T0, T1> zip(result0: Result<T0>, result1: Result<T1>): Result<Pair<T0, T1>> =
            result0.flatMap { t0 ->
                result1.map { t1 ->
                    Pair(t0, t1)
                }
            }

    }


}