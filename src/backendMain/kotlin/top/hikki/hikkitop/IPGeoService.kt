package top.hikki.hikkitop

import IPGeoMain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.remoteAddressOrNull

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class IPGeoService: IIPGeoService{

    val ipGeoApi = IPGeoMain()

    @Autowired
    lateinit var serverRequest: ServerRequest

    override suspend fun getIPInfo(): String {
        return ipGeoApi.getIPInfo(serverRequest.remoteAddressOrNull().toString().substring(1))
    }

}