package top.hikki.hikkitop.service

import TtsMain
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class ReverserService : IReverserService {
    val reverser = TtsMain()

    val prefix = "/tmp/"
    val suffix = "wav"

    override suspend fun ping(message: String): List<String> {
        return mutableListOf("$message Hello world from server!")
    }

    override suspend fun textReverse(text: String): String {
        val filename = System.currentTimeMillis().toString()+(0..10000).random()
        return reverser.getReversedAudio(prefix, suffix, text, filename)
    }

    override suspend fun audioReverse(base64Str: String): String {
        val filename = System.currentTimeMillis().toString()+(0..10000).random()
        return reverser.reverseAudioByBase64(prefix, suffix, filename, base64Str)
    }

}