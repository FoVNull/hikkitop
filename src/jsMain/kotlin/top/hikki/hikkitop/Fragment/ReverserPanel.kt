package top.hikki.hikkitop.Fragment

import io.kvision.form.text.textAreaInput
import io.kvision.html.*
import io.kvision.panel.SimplePanel
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLTextAreaElement
import top.hikki.hikkitop.AppScope
import top.hikki.hikkitop.Model.ReverserModel

internal val reverserTabPanel = SimplePanel {
    ul(className = "nav nav-pills") {
        role = "tablist"
        li(className = "nav-item") {
            role = "presentation"
            link("Reverse by text input", "#reverser", className = "nav-link active") {
                role = "tab"
                setAttribute("data-bs-toggle", "tab")
                setAttribute("aria-selected", "true")
            }
        }
        li(className = "nav-item") {
            role = "presentation"
            link("Reverse by record", "#recorder", className = "nav-link") {
                role = "tab"
                setAttribute("data-bs-toggle", "tab")
                setAttribute("aria-selected", "false")
                setAttribute("tabindex", "-1")
            }
        }
    }
    br()
    div(className = "tab-content") {
        div(className = "tab-pane fade show active") {
            id = "reverser"
            role = "tabpanel"
            p() { setAttribute("id", "textAreaInfo") }
            div(className = "form-group") {
                val textInputInfo = document.getElementById("textAreaInfo")
                textInputInfo?.innerHTML =
                    "<p>Input Text <font size=2>(Based on <a href='https://cloud.baidu.com/product/speech/tts_online'>Baidu TTS</a>)</font></p>"
                textAreaInput(rows = 3, className = "form-control") {
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
                            if (textAreaE.value == "") {
                                textAreaE.setCustomValidity("Input couldn't be empty")
                                textAreaE.reportValidity()
                            } else {
                                val barElement = document.getElementById("process-bar-1") as HTMLElement
                                barElement.hidden = false
                                barElement.innerHTML =
                                    "<p class='text-warning' style='float:left;'><b>processing...</b></p><img src='static/Pulse.gif' width='50px'>"

                                val audioBase64 = ReverserModel.getAudioByText(textAreaE.value)
                                val audioContainer = document.getElementById("audio-container-1") as HTMLElement
                                audioContainer.innerHTML =
                                    "<audio id='audio-player' controls><source type='audio/wav' src='$audioBase64'></audio>"
                                barElement.innerHTML =
                                    "<img src='static/done.png' width='40px' style='float:left'><p class='text-success'><b>Done</b></p>"
                            }
                        }
                    }
                }
            }
            div() {
                setAttribute("hidden", "")
                id = "process-bar-1"
            }
            div() { id = "audio-container-1" }
        }
        div(className = "tab-pane fade") {
            id = "recorder"
            role = "tabpanel"
            div() {
                id = "recorder"
                div {
                    id = "controls"
                    button("Start", className = "btn btn-sm btn-info") {
                        id = "record-btn"
                    }
                    button("Pause", className = "btn btn-sm btn-danger") {
                        id = "pause-btn"
                        disabled = true
                    }
                    button("Stop", className = "btn btn-sm btn-primary") {
                        id = "stop-btn"
                        disabled = true

                    }
                }
            }
            div {
                id = "recordingsList"
                setAttribute("hidden", "")
            }
            div(className = "btn-set") {
                button("Convert", className = "btn btn-secondary long-btn") {
                    onClick {
                        AppScope.launch {
                            val barElement = document.getElementById("process-bar-2") as HTMLElement
                            barElement.hidden = false
                            barElement.innerHTML = "<p class='text-warning' style='float:left;'><b>processing...</b></p><img src='static/Pulse.gif' width='50px'>"

                            val base64 = document.getElementById("recordingsList") as HTMLElement
                            val base64Str = base64.innerText.split(",")[1]
                            val audioBase64 = ReverserModel.getAudioByFile(base64Str)
                            val audioContainer = document.getElementById("audio-container-2") as HTMLElement
                            audioContainer.innerHTML = "<audio id='audio-player' controls><source type='audio/wav' src='$audioBase64'></audio>"
                            barElement.innerHTML = "<img src='static/done.png' width='40px' style='float:left'><p class='text-success'><b>Done</b></p>"
                        }
                    }
                }
            }
            div() {
                setAttribute("hidden", "")
                id = "process-bar-2"
            }
            div() { id = "audio-container-2" }
        }
    }
}
