package top.hikki.hikkitop.service

import io.kvision.annotations.KVService

@KVService
interface IReverserService {
    // test
    suspend fun ping(message: String): List<String>
    // send text data
    suspend fun textReverse(text: String): String
    // reverse audio file directly
    suspend fun audioReverse(base64Str: String): String
}
