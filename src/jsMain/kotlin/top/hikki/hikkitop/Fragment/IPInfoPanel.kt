package top.hikki.hikkitop.Fragment

import io.kvision.html.*
import io.kvision.panel.SimplePanel
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLElement
import top.hikki.hikkitop.AppScope
import top.hikki.hikkitop.Model.IPGeoModel
import kotlin.js.Date

internal val IPInfoPanel = SimplePanel {
    h5("IP service provided by <a href='https://ipgeolocation.io/'>ipgeolocation.io</a>", rich=true)
    p("<font color='#D3D3D3'>This site won't save your IP information</font>", rich=true)
    AppScope.launch {
        val ipInfo = IPGeoModel.getIPInfo()
        val district = ipInfo["district"] as String?
        val city = ipInfo["city"] as String?
        val countryCode = ipInfo["countryCode"] as String?
        val statusCode = ipInfo["code"] as String?
        val offset = ipInfo["timeOffset"] as Int?
        val isp = ipInfo["isp"] as String?

        div(className = "card border-primary mb-3") {
            div("IP Address", className = "card-header")
            div(className = "card-body") {
                h4(ipInfo["ipaddr"] as String?, className = "card-title")
                p(ipInfo["hostname"] as String?, className = "card-text")
            }
        }
        div(className = "card border-primary mb-3") {
            div("Location", className = "card-header")
            div(className = "card-body") {
                h4(ipInfo["countryName"] as String?, className = "card-title")
                image(ipInfo["countryFlag"] as String?) { setAttribute("style", "float:left;width:25px") }
                p("  $district, $city, $countryCode", className = "card-text")
            }
        }
        div(className = "card border-primary mb-3") {
            div("ISP", className = "card-header")
            div(className = "card-body") {
                h4(isp!!.padEnd(20, ' '), className = "card-title")
                p(ipInfo["organization"] as String?, className = "card-text")
            }
        }
        div(className = "last-card card border-primary mb-3") {
            setAttribute("style", "float: none")
            div("Local Time", className = "card-header")
            div(className = "card-body") {
                h4("Based on your IP location", className = "card-title")
                p(className = "card-text") { setAttribute("id", "clock") }
            }
            window.setInterval(handler = {
                if (document.getElementById("clock") != null) {
                    val clock = document.getElementById("clock") as HTMLElement
                    val localOffset = Date().getTimezoneOffset()
                    val utc = Date().getTime() + localOffset.times(60 * 1000)
                    val targetDate = Date(utc.plus(offset?.times(3600 * 1000) ?: 0))
                    val offsetStr = if (offset!! >= 0) "UTC+$offset" else "UTC$offset"
                    clock.innerText = dateFormat(targetDate) + " $offsetStr"
                }
            }, 100)
        }
        if(statusCode!="200"){
            add(div("Backend exception (status code $statusCode), you can try again or drop me a line by "+
                    "<a href='https://marshmallow-qa.com/fovnull?utm_medium=url_text&utm_source=promotion'>marshmallow</a> or email."
                , rich=true))
        }
    }
}

private fun dateFormat(date: Date): String {
    val year = date.getFullYear()
    val month = if (date.getMonth() + 1 < 10) "0" + (date.getMonth() + 1) else (date.getMonth() + 1)
    val day = if (date.getDate() + 1 < 10) "0" + date.getDate() else date.getDate()
    val hour = if (date.getHours() < 10) "0" + date.getHours() else date.getHours()
    val minute = if (date.getMinutes() < 10) "0" + date.getMinutes() else date.getMinutes()
    val second = if (date.getSeconds() < 10) "0" + date.getSeconds() else date.getSeconds()

    return "$year/$month/$day $hour:$minute:$second"
}
