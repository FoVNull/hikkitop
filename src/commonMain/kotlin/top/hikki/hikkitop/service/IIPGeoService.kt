package top.hikki.hikkitop.service

import io.kvision.annotations.KVService

@KVService
interface IIPGeoService {
    // return json str
    suspend fun getIPInfo():String
}
