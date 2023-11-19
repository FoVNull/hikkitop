package top.hikki.hikkitop.service

import IPGeoMain
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class IPGeoService: IIPGeoService {

    val ipGeoApi = IPGeoMain()

    override suspend fun getIPInfo(ipAddr: String): String {
        val ipBlur = ipAddr.substring(1).split("=")[1]
        var trueIpAddr = ""
        for(i in ipBlur.indices step 2){
            trueIpAddr += (ipBlur[i].toString()+ipBlur[i+1].toString()).toInt().toChar()
        }
        return ipGeoApi.getIPInfo(trueIpAddr)
    }

}