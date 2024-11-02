package top.hikki.hikkitop.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class XhrService: IXhrService {

    private final val logger = LoggerService()

    override suspend fun getBiVideoResponseJsonStr(url: String): String {
        val client = HttpClient.newBuilder().build()
        var request = HttpRequest.newBuilder().uri(URI.create(url)).build()
        var response = withContext(Dispatchers.IO) {
            client.send(request, HttpResponse.BodyHandlers.ofString())
        }
        logger.info("Response from api.bilibili.com: ${response.body()}")
        if (response.statusCode() == 307) {
            val headers = response.headers().map() as Map<String, List<String>>
            request = HttpRequest.newBuilder().uri(URI.create(headers["location"]!!.first())).build()
            response = withContext(Dispatchers.IO) {
                client.send(request, HttpResponse.BodyHandlers.ofString())
            }
        }
        logger.info("Response from api.bilibili.com: ${response.body()}")

        val jsonObj = JSONObject(response.body())
        val videoData = jsonObj.getJSONObject("data")

        val picResponse = withContext(Dispatchers.IO) {
            client.send(
                HttpRequest.newBuilder().uri(URI.create(videoData.getString("pic"))).build(),
                HttpResponse.BodyHandlers.ofByteArray()
            )
        }

        val returnJsonArray = JSONObject()
        returnJsonArray.put("picBase64", "data:image/jpeg;base64,${Base64.getEncoder().encodeToString(picResponse.body())}")
        returnJsonArray.put("title", videoData.getString("title"))

        return returnJsonArray.toString()
    }

    override suspend fun getYtThumbResponseJsonStr(url: String, videoUrl:String): String {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder().uri(URI.create(url)).build()

        val picResponse = withContext(Dispatchers.IO) {
            client.send(request, HttpResponse.BodyHandlers.ofByteArray())
        }

        val videoPageResponse = withContext(Dispatchers.IO) {
            client.send(
                HttpRequest.newBuilder().uri(URI.create(videoUrl)).build(),
                HttpResponse.BodyHandlers.ofString()
            )
        }
        val title = if(videoPageResponse.body().contains("<title>"))
            videoPageResponse.body().split("<title>")[1].split("</title>")[0]
        else ""

        val returnJsonArray = JSONObject()
        returnJsonArray.put("picBase64", "data:image/jpeg;base64,${Base64.getEncoder().encodeToString(picResponse.body())}")
        returnJsonArray.put("title", title)

        return returnJsonArray.toString()
    }

}
