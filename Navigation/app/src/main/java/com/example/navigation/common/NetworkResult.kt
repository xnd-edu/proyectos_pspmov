package com.example.navigation.common


sealed class NetworkResult<T> {

    class Success<T>(val data: T) : NetworkResult<T>()

    class Error<T>(val error: NetworkError) : NetworkResult<T>()

    class Loading<T> : NetworkResult<T>()

    inline fun <R> map( transform :(data: T) -> R) : NetworkResult<R> =
        when(this){
            is Error -> Error(error)
            is Loading -> Loading()
            is Success -> Success(transform(data))
        }
    inline fun <R> then(transform: (data: T) -> NetworkResult<R>): NetworkResult<R> =
        when (this) {
            is Error -> Error(error)
            is Loading -> Loading()
            is Success -> transform(data)
        }
}

fun <T> List<NetworkResult<T>>.combine(): NetworkResult<List<T>> {
    val successData = mutableListOf<T>()

    for (result in this) {
        when (result) {
            is NetworkResult.Success -> successData.add(result.data)
            is NetworkResult.Error -> return NetworkResult.Error(result.error)
            is NetworkResult.Loading -> return NetworkResult.Loading()
        }
    }
    return NetworkResult.Success(successData)
}

