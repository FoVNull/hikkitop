package top.hikki.hikkitop.Model

import io.kvision.remote.getService

import top.hikki.hikkitop.service.IIPGeoService
import kotlin.js.Json

object IPGeoModel {

    private val ipGeoService = getService<IIPGeoService>()

    suspend fun getIPInfo(): Json{
        return JSON.parse(ipGeoService.getIPInfo())
    }
}
