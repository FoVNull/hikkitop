package top.hikki.hikkitop.Fragment

import io.kvision.core.onClick
import io.kvision.html.*
import io.kvision.panel.SimplePanel
import kotlinx.browser.window
import kotlin.js.Date

val topPanel = SimplePanel {
    h2("HIKKI-TOP").onClick { window.location.href="/" }
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
            p("©"+ Date().getFullYear()+" Made by <a href='/'>FoVNull</a>.", rich=true)
            p("Powered by <a href='https://kvision.io/'>KVision</a>.", rich=true)
            p("Theme based on <a href=\"https://bootswatch.com/quartz/\" rel=\"nofollow\">Bootswatch</a>.", rich=true)
        }
    }
}