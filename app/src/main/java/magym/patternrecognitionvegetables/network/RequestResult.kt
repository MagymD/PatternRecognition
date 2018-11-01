package magym.patternrecognitionvegetables.network

sealed class RequestResult<T> {

    class Result<T>(val data: T) : RequestResult<T>()

    class Error<T>(val error: Throwable) : RequestResult<T>()

}