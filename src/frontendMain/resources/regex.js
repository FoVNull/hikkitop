function executeRegex(){
    const exp = document.getElementById("reg-expression").value
    const text = document.getElementById("regex-input-text").value.toString()
    if(exp!=="" && text!=="") {
        const re = new RegExp(exp);
        const res = text.match(re)
        document.getElementById("result-display").innerHTML = res.join("")
    }
}