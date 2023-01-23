package top.hikki.hikkitop

import io.kvision.annotations.KVService

@KVService
interface IIPGeoService {
    // return json str
    suspend fun getIPInfo(ipAddr: String):String
}