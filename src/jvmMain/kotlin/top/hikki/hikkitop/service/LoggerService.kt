package top.hikki.hikkitop.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class LoggerService : ILoggerService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun info(message: String) {
        logger.info(message)
    }

    override fun error(message: String) {
        logger.error(message)
    }
}