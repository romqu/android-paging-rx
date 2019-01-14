package de.romqu.androidpagingrx.common.extension

import io.reactivex.Single
import de.romqu.androidpagingrx.core.Result

fun <T : Any> T?.toResult(): Result<T> = when {
    this == null -> Result.failure("is null")
    else -> Result.success(this )
}

fun <T : Any> T?.toSingleResult(): Single<Result<T>> =
        toResult().asSingleFromCallable()