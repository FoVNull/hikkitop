package top.hikki.hikkitop.Data

/**
 * The card element on index page.
 *
 * @property header class="card-header"
 * @property title class="card-title"
 * @property text description of feature, each element will be filled into <li>
 * @property target link of the detail page
 */
data class FeatureCard (
    val header: String,
    val title: String,
    val text: List<String>,
    val target: String
) {

}
