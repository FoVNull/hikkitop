package top.hikki.hikkitop.Model

import top.hikki.hikkitop.service.ReverserService

object ReverserModel {

    private val reverserService = ReverserService()

    suspend fun getAudioByText(text: String): String{
        val audioBytes = reverserService.textReverse(text)
        return "data:audio/wav;base64,$audioBytes"
    }

    suspend fun getAudioByFile(base64Str: String): String{
        val audioBytes = reverserService.audioReverse(base64Str)
        return "data:audio/wav;base64,$audioBytes"
    }

}
