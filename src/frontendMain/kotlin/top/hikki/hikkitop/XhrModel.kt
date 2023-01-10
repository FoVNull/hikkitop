package top.hikki.hikkitop

import io.kvision.remote.getService

object XhrModel {

    private val xhrService = getService<IXhrService>()

    var responseJsonStr = ""

    suspend fun getBiVideoResponse(url: String){
        responseJsonStr = xhrService.getBiVideoResponseJsonStr(url)
    }

    suspend fun getYtThumbResponse(url: String, videoUrl: String){
        responseJsonStr = xhrService.getYtThumbResponseJsonStr(url, videoUrl)
    }
}