package top.hikki.hikkitop

import io.kvision.remote.getService
import io.kvision.utils.syncWithList

object ReverserModel {

    private val reverserService = getService<IReverserService>()

    val pingMsgList = mutableListOf<String>()
    var audioBase64 = ""

    suspend fun ping(message: String) {
        val pingMsg = reverserService.ping(message)
        pingMsgList.syncWithList(pingMsg)
    }

    suspend fun getAudioByText(text: String){
        val audioBytes = reverserService.textReverse(text)
        audioBase64="data:audio/wav;base64,$audioBytes"
    }

    suspend fun getAudioByFile(base64Str: String){
        val audioBytes = reverserService.audioReverse(base64Str)
        audioBase64="data:audio/wav;base64,$audioBytes"
    }

}
