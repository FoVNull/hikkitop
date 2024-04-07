package top.hikki.hikkitop

import io.kvision.Application
import io.kvision.CoreModule
import io.kvision.module
import io.kvision.panel.root
import io.kvision.startApplication
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import top.hikki.hikkitop.Fragment.IPInfoPanel
import top.hikki.hikkitop.Fragment.regexPanel
import top.hikki.hikkitop.Fragment.reverserTabPanel
import top.hikki.hikkitop.Fragment.thumbnailsPanel

val AppScope = CoroutineScope(window.asCoroutineDispatcher());

class App: Application() {
    init {
        io.kvision.require("./bootstrap.min.css")
        io.kvision.require("./overall.css")
    }
    override fun start() {
        root("hello"){
            add(topPanel)
        }
        root("search-box") {
            add(searchboxPanel)
        }
        root("toolkit") {
            buildToolkit(cardList)
            add(toolkitPanel)
        }

        root("reverser-tab") {
            add(reverserTabPanel)
        }

        root("ip-info") {
            add(IPInfoPanel)
        }
        root("thumbnails-download") {
            add(thumbnailsPanel)
        }
        root("regex-playground") {
            add(regexPanel)
        }
        root("footer") {
            add(footerPanel)
        }
    }
}

fun main() {
    startApplication(::App, module.hot, CoreModule)
}