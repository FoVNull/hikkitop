package top.hikki.hikkitop.Model

import io.kvision.remote.getService
import top.hikki.hikkitop.service.IXhrService
import kotlin.js.Json

object XhrModel {

    private val xhrService = getService<IXhrService>()

    suspend fun getBiVideoResponse(url: String): Json {
        return JSON.parse(xhrService.getBiVideoResponseJsonStr(url))
    }

    suspend fun getYtThumbResponse(url: String, videoUrl: String): Json {
        return JSON.parse(xhrService.getYtThumbResponseJsonStr(url, videoUrl))
    }
}
