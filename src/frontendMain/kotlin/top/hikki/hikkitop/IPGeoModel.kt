package top.hikki.hikkitop

import io.kvision.remote.getService

object IPGeoModel {

    private val ipGeoService = getService<IIPGeoService>()

    var ipInfo = ""

    suspend fun getIPInfo(ipAddr: String){
        ipInfo = ipGeoService.getIPInfo(ipAddr)
    }
}