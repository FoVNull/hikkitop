package top.hikki.hikkitop.Fragment

import io.kvision.html.*
import io.kvision.panel.SimplePanel
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import top.hikki.hikkitop.AppScope
import top.hikki.hikkitop.Data.VideoSite
import top.hikki.hikkitop.Model.XhrModel

internal val thumbnailsPanel = SimplePanel {
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
                            val fileName = (1..8).map{(('a'..'z') + ('A'..'Z') + ('0'..'9')).random()}.joinToString("")
                            val response = XhrModel.getBiVideoResponse(url)
                            siteInfoElement.innerHTML = "<h5 class='modal-title'>${response["title"]} - Bilibili</h5>"
                            imgURLElement.innerHTML = "<img download='$fileName .jpg' src='${response["picBase64"]}' width='100%'>"
                            downloadBtn.innerHTML = "<a download='$fileName .jpg' href='${response["picBase64"]}'><button class='btn btn-primary'>Save</button></a>"
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
