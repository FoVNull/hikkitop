package top.hikki.hikkitop

import IPGeoMain
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class IPGeoService: IIPGeoService{

    val ipGeoApi = IPGeoMain()

    override suspend fun getIPInfo(): String {
        return ipGeoApi.ipInfo
    }

}