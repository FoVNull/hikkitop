package top.hikki.hikkitop.Model

import top.hikki.hikkitop.service.IPGeoService
import kotlin.js.Json

object IPGeoModel {

    private val ipGeoService = IPGeoService()

    suspend fun getIPInfo(): Json{
        return JSON.parse(ipGeoService.getIPInfo())
    }
}
