package top.hikki.hikkitop.Model

import io.kvision.remote.getService
import top.hikki.hikkitop.service.IReverserService

object ReverserModel {

    private val reverserService = getService<IReverserService>()

    suspend fun getAudioByText(text: String): String{
        val audioBytes = reverserService.textReverse(text)
        return "data:audio/wav;base64,$audioBytes"
    }

    suspend fun getAudioByFile(base64Str: String): String{
        val audioBytes = reverserService.audioReverse(base64Str)
        return "data:audio/wav;base64,$audioBytes"
    }

}
