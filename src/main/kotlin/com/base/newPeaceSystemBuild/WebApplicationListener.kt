package com.base.newPeaceSystemBuild

import com.base.newPeaceSystemBuild.util.Ut
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component


@Component
@Order(0)
class WebApplicationListener: ApplicationListener<ApplicationReadyEvent> {

    @Value("\${custom.aligo.userId}")
    private val aligoUserId: String? = null

    @Value("\${custom.aligo.apiKey}")
    private val aligoApiKey: String? = null

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        Ut.initAligo(aligoUserId!!, aligoApiKey!!)
    }
}