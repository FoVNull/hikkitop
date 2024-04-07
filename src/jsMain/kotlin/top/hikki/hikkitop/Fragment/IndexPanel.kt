package top.hikki.hikkitop

import io.kvision.core.onClick
import io.kvision.core.onInput
import io.kvision.html.*
import io.kvision.panel.SimplePanel
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLInputElement
import top.hikki.hikkitop.Data.FeatureCard
import kotlin.js.Date

val cardList = arrayOf(
    FeatureCard(
        "Audio Reverser", "Reverse your audio",
        listOf("Text to reversed audio", "Reverse your voice by recording"),
        "reverser.html"
    ),
    FeatureCard(
        "Your IP", "IP information",
        listOf("This site won't save your IP information", "Could be used to test your proxy"),
        "ipgeo.html"
    ),
    FeatureCard(
        "Thumbnail download", "Download video thumbnail",
        listOf("Youtube", "Bilibili"),
        "thumbnails.html"
    ),
    FeatureCard(
        "Regex playground", "Test regular expressions",
        listOf(""),
        "regex.html"
    )
)

val topPanel = SimplePanel {
    h2("HIKKI-TOP").onClick { window.location.href="/" }
}

val searchboxPanel = SimplePanel {
    div(className = "container mt-5") { div(className = "row justify-content-center") { div(className = "col-md-10") {
        div(className = "input-group") {
            input(className = "form-control mr-sm-2", type = InputType.SEARCH) {
                id = "search-input"
                placeholder = "Search"
                setAttribute("aria-label", "Search")
                onInput {
                    val inputText = (document.getElementById("search-input") as HTMLInputElement).value
                    val cardIdxList = searchCard(inputText)
                    toolkitPanel.removeAll()
                    if (cardIdxList != null) {
                        buildToolkit(cardIdxList.map { idx -> cardList[idx] }.toTypedArray())
                    } else {
                        buildToolkit(cardList)
                    }
                }
            }
        }
    }}}
}

var toolkitPanel = SimplePanel {}

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

fun buildToolkit(cardList: Array<FeatureCard>){
    for (card in cardList) {
        toolkitPanel.add(SimplePanel {
            div(className = "card border-secondary mb-3") {
                onClick { window.open(card.target) }
                div(className = "card-header", content = card.header)
                div(className = "card-body") {
                    h4(className = "card-title", content = card.title)
                    ul(className = "card-text") {
                        card.text.forEach { line -> li(line) }
                    }
                }
            }
        })
    }
}

private fun searchCard(query: String?): List<Int>? {
    if (query.isNullOrBlank()) {
        return null
    }
    val candidates: List<Pair<Int, Double>> = cardList.mapIndexed { idx, card ->
        Pair(idx, getSearchWeight(card, query))
    }
    return candidates.sortedBy { it.second }.map { it.first }.take(3)
}

private fun getSearchWeight(card: FeatureCard, query: String): Double {
    var weight = 1.0
    card.header.split(" ").forEach { word -> weight *= levenshteinDistance(word, query) }
    // card.title.split(" ").forEach { word -> weight *= levenshteinDistance(word, query) * .5 }
    return weight * .1
}
private fun levenshteinDistance(str1: String, str2: String): Int {
    val word = str1.lowercase()
    val query = str2.lowercase()
    val len1 = word.length
    val len2 = query.length
    val dp = Array(len1 + 1) { IntArray(len2 + 1) }

    for (i in 0..len1) {
        for (j in 0..len2) {
            if (i == 0) {
                dp[i][j] = j
            } else if (j == 0) {
                dp[i][j] = i
            } else if (word[i - 1] == query[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1]
            } else {
                dp[i][j] = 1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
            }
        }
    }
    return dp[len1][len2]
}