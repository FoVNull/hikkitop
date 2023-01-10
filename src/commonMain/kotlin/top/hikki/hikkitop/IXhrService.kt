package top.hikki.hikkitop

import io.kvision.annotations.KVService

@KVService
interface IXhrService {
    suspend fun getBiVideoResponseJsonStr(url: String):String
    suspend fun getYtThumbResponseJsonStr(url: String, videoUrl: String):String
}