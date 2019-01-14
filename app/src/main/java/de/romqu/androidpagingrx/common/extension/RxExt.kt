package de.romqu.androidpagingrx.common.extension

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

fun <T> T.asObservable(): Observable<T> = Observable.just<T>(this)


fun <T> PublishSubject<T>.toObservable() =
    toFlowable(BackpressureStrategy.BUFFER)
        .toObservable()

fun <T> BehaviorSubject<T>.toObservable() =
    toFlowable(BackpressureStrategy.BUFFER)
        .toObservable()

fun <T : Any> T.asSingleFromCallable(): Single<T> =
    Single.fromCallable { this }
