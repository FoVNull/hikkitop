package top.hikki.hikkitop.Model

import top.hikki.hikkitop.service.XhrService
import kotlin.js.Json

object XhrModel {

    private val xhrService = XhrService()

    suspend fun getBiVideoResponse(url: String): Json {
        return JSON.parse(xhrService.getBiVideoResponseJsonStr(url))
    }

    suspend fun getYtThumbResponse(url: String, videoUrl: String): Json {
        return JSON.parse(xhrService.getYtThumbResponseJsonStr(url, videoUrl))
    }
}
