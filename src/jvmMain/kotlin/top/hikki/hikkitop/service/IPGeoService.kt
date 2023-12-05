package top.hikki.hikkitop.service

import IPGeoMain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class IPGeoService: IIPGeoService {

    @Autowired
    lateinit var env: Environment

    @Autowired
    lateinit var serverRequest: ServerRequest

    override suspend fun getIPInfo(): String {
        val ipGeoApi = IPGeoMain(env["HIKKI_KEY"])
        return ipGeoApi.getIPInfo(serverRequest.remoteAddress().get().address.hostAddress)
    }

}
