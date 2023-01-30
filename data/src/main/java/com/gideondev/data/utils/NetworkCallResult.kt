package com.gideondev.data.utils

sealed interface NetworkCallResult<T : Any>

class ApiSuccess<T : Any>(val data: T) : NetworkCallResult<T>
class ApiError<T : Any>(val code: Int, val message: String?) : NetworkCallResult<T>
class ApiException<T : Any>(val e: Throwable) : NetworkCallResult<T>