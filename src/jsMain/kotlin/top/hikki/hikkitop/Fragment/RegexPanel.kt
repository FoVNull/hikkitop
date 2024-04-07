package top.hikki.hikkitop.Fragment

import io.kvision.form.text.textAreaInput
import io.kvision.html.br
import io.kvision.html.div
import io.kvision.html.h5
import io.kvision.panel.SimplePanel

internal val regexPanel = SimplePanel{
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