package top.hikki.hikkitop

import io.kvision.Application
import io.kvision.CoreModule
import io.kvision.core.onClick
import io.kvision.form.text.textAreaInput
import io.kvision.html.*
import io.kvision.module
import io.kvision.panel.SimplePanel
import io.kvision.panel.root
import io.kvision.startApplication
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import top.hikki.hikkitop.Data.VideoSite
import top.hikki.hikkitop.Model.IPGeoModel
import top.hikki.hikkitop.Model.ReverserModel
import top.hikki.hikkitop.Model.XhrModel
import kotlin.js.Date
import kotlin.js.RegExp

val AppScope = CoroutineScope(window.asCoroutineDispatcher());

class App: Application() {
    init {
        io.kvision.require("./bootstrap.min.css")
        io.kvision.require("./overall.css")
    }
    override fun start() {
        root("hello"){
            h2("HIKKI-TOP").onClick { window.location.href="/" }
        }
        root("reverser"){
            p(){setAttribute("id", "textAreaInfo")}
            div(className = "form-group") {
                val textInputInfo = document.getElementById("textAreaInfo")
                textInputInfo?.innerHTML = "<p>Input Text <font size=2>(Based on <a href='https://cloud.baidu.com/product/speech/tts_online'>Baidu TTS</a>)</font></p>"
                textAreaInput(rows=3, className = "form-control") {
                    setAttribute("id", "textArea1")
                    setAttribute("maxlength", "50")
                    setAttribute("placeholder", "哎呀米诺")
                }
            }
            div(className = "btn-set") {
                button("Convert", className = "btn btn-secondary long-btn") {
                    onClick {
                        val textAreaE = document.getElementById("textArea1") as HTMLTextAreaElement
                        AppScope.launch {
                            val barElement = document.getElementById("process-bar") as HTMLElement
                            barElement.hidden = false
                            barElement.innerHTML = "<p class='text-warning' style='float:left;'><b>processing...</b></p><img src='static/Pulse.gif' width='50px'>"

                            val audioBase64 = ReverserModel.getAudioByText(textAreaE.value)
                            val audioContainer = document.getElementById("audio-container") as HTMLElement
                            audioContainer.innerHTML = "<audio id='audio-player' controls><source type='audio/wav' src='$audioBase64'></audio>"
                            barElement.innerHTML = "<img src='static/done.png' width='40px' style='float:left'><p class='text-success'><b>Done</b></p>"
                        }
                    }
                }
            }
        }
        root("recoder-btn"){
            div(className = "btn-set") {
                button("Convert", className = "btn btn-secondary long-btn") {
                    onClick {
                        AppScope.launch {
                            val barElement = document.getElementById("process-bar") as HTMLElement
                            barElement.hidden = false
                            barElement.innerHTML = "<p class='text-warning' style='float:left;'><b>processing...</b></p><img src='static/Pulse.gif' width='50px'>"

                            val base64 = document.getElementById("recordingsList") as HTMLElement
                            val base64Str = base64.innerText.split(",")[1]
                            val audioBase64 = ReverserModel.getAudioByFile(base64Str)
                            val audioContainer = document.getElementById("audio-container") as HTMLElement
                            audioContainer.innerHTML = "<audio id='audio-player' controls><source type='audio/wav' src='$audioBase64'></audio>"
                            barElement.innerHTML = "<img src='static/done.png' width='40px' style='float:left'><p class='text-success'><b>Done</b></p>"
                        }
                    }
                }
            }
        }
        root("ip-info"){
            h5("IP service provided by <a href='https://ipgeolocation.io/'>ipgeolocation.io</a>", rich=true)
            p("<font color='#D3D3D3'>This site won't save your IP information</font>", rich=true)
            AppScope.launch {
                if(window.location.search!="") {
                    val ipInfo = IPGeoModel.getIPInfo(window.location.search)
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
        }
        root("thumbnails-download"){
            add(thumbnailsPanel)
        }
        root("regex-playground"){
            add(regexPanel)
        }
        root("footer"){
            add(footerPanel)
        }
    }
    private fun dateFormat(date: Date): String{
        val Y = date.getFullYear()
        val M = if(date.getMonth()+1<10) "0"+(date.getMonth()+1) else (date.getMonth()+1)
        val D = if(date.getDate()+1<10) "0"+date.getDate() else date.getDate()
        val h = if(date.getHours()<10) "0"+date.getHours() else (date.getHours())
        val m = if(date.getMinutes()<10) "0"+date.getMinutes() else (date.getMinutes())
        val s = if(date.getSeconds()<10) "0"+date.getSeconds() else (date.getSeconds())

        return "$Y/$M/$D $h:$m:$s"
    }
}

//  write html component separately; make start() clean
val thumbnailsPanel = SimplePanel{
    h5("Support bilibili and youtube.", rich=true)
    p("<font color='#D3D3D3'>You can click save button to download. <br> Support youtube video download link (y2mate)</font>", rich=true)
    div(className = "input-group mb-3") {
        input(className = "form-control") {
            setAttribute("id", "url-text")
            setAttribute("type", "text")
            setAttribute("placeholder", "paste url of video")
            setAttribute("aria-describedby", "btn-submit-url")
        }
        button("download", className = "btn btn-primary"){
            setAttribute("id", "btn-submit-url")
            onClick {
                val urlText = document.getElementById("url-text") as HTMLInputElement

                val thumbnailPanel = document.getElementById("thumbnail-panel") as HTMLElement
                var site: VideoSite = VideoSite.UNKNOWN
                val siteInfoElement = document.getElementById("site-info") as HTMLElement
                val imgURLElement = document.getElementById("thumbnail-url") as HTMLElement
                val downloadBtn = document.getElementById("btn-download") as HTMLElement

                thumbnailPanel.hidden = true
                AppScope.launch {
                    val url = urlText.value
                    if (url.contains("youtube")){
                        site = VideoSite.YOUTUBE
                    }else if(url.contains("bilibili")){
                        site = VideoSite.BILIBILI
                    }

                    val queryString: String = url.split("?")[1]
                    when(site){
                        VideoSite.YOUTUBE -> {
                            val params = queryString.split("&")
                            for(param in params){
                                val kv = param.split("=")
                                check(kv.size == 2 ){"Query string parse wrong."}
                                if(kv[0] == "v") {
                                    val imgURL = "https://i.ytimg.com/vi/${kv[1]}/maxresdefault.jpg"
                                    val response = XhrModel.getYtThumbResponse(imgURL, "https://www.youtube.com/watch?v=${kv[1]}")
                                    siteInfoElement.innerHTML = "<h5 class='modal-title'>${response["title"]}</h5>"
                                    imgURLElement.innerHTML = "<img src='${response["picBase64"]}' width='100%'>"
                                    downloadBtn.innerHTML = "<a download='${kv[1]}.jpg' href='${response["picBase64"]}'><button class='btn btn-primary'>Save</button></a>"+
                                            "<a href='https://youtubepi.com/watch?v=${kv[1]}' target='_blank'><button class='btn btn-primary'>y2mate</button></a>"
                                    break
                                }
                            }
                        }
                        VideoSite.BILIBILI -> {
                            val pattern = RegExp("[a-zA-z]+://")
                            val bv = if(pattern.test(url))
                                url.split("://")[1].split("/")[2]
                            else
                                url.split("/")[2]

                            val response = XhrModel.getBiVideoResponse("http://api.bilibili.com/x/web-interface/view?bvid=$bv")
                            siteInfoElement.innerHTML = "<h5 class='modal-title'>${response["title"]} - Bilibili</h5>"
                            imgURLElement.innerHTML = "<img download='$bv.jpg' src='${response["picBase64"]}' width='100%'>"
                            downloadBtn.innerHTML = "<a download='$bv.jpg' href='${response["picBase64"]}'><button class='btn btn-primary'>Save</button></a>"
                        }
                        VideoSite.UNKNOWN -> {
                            siteInfoElement.innerHTML = "<h5 class='modal-title'>Unknown site</h5>"
                            imgURLElement.innerHTML = "<img src='' width='100%'>"
                        }
                    }

                    thumbnailPanel.hidden = false
                }
            }
        }
    }
    div(){
        setAttribute("hidden", "true")
        setAttribute("id", "thumbnail-panel")
        div (className="modal-dialog"){
            setAttribute("role", "document")
            div(className = "modal-content"){
                div(className = "modal-header"){
                    setAttribute("id", "site-info")
                }
                div(className = "modal-body"){setAttribute("id", "thumbnail-url")}
                div(className = "modal-footer"){setAttribute("id", "btn-download")}
            }
        }
    }
}

val regexPanel = SimplePanel{
    h5("<b>Regular expression</b>", rich=true)
    div(className = "form-group") {
        textAreaInput(rows=2, className = "form-control") {
            setAttribute("onkeyup", "executeRegex()")
            setAttribute("id", "reg-expression")
            setAttribute("maxlength", "100")
            setAttribute("placeholder", "Enter regular expression")
        }
    }
    br()
    h5("<b>Matching text</b>", rich=true)
    div(className = "form-group") {
        textAreaInput(rows=7, className = "form-control") {
            setAttribute("onkeyup", "executeRegex()")
            setAttribute("id", "regex-input-text")
            setAttribute("maxlength", "500")
            setAttribute("placeholder", "Enter text you want match.")
        }
    }
    br()
    h5("<b>Matched</b>", rich=true)
    div(className = "form-group") {
        textAreaInput(rows=4, className = "form-control") {
            setAttribute("id", "result-display")
            setAttribute("readonly", "")
        }
    }
}

val footerPanel = SimplePanel{
    div(className = "row"){
        div(className = "col-lg-12"){
            ul(className = "list-unstyled"){
                li(className = "float-end"){link("Back to top", "#top")}
                li{link("Twitter", "https://twitter.com/FoVNull")}
                li{link("GitHub", "https://github.com/FoVNull")}
                li("Anonymous bugs report: <a href='https://marshmallow-qa.com/fovnull?utm_medium=url_text&utm_source=promotion'>marshmallow</a>", rich=true)
            }
            p("©"+Date().getFullYear()+" Made by <a href='/'>FoVNull</a>.", rich=true)
            p("Powered by <a href='https://kvision.io/'>KVision</a>.", rich=true)
            p("Theme based on <a href=\"https://bootswatch.com/quartz/\" rel=\"nofollow\">Bootswatch</a>.", rich=true)
        }
    }
}

fun main() {
    startApplication(::App, module.hot, CoreModule)
}