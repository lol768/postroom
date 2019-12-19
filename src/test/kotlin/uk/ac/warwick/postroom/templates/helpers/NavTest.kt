package uk.ac.warwick.postroom.templates.helpers

import freemarker.template.Configuration
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.ac.warwick.postroom.context.ContextTest
import uk.ac.warwick.postroom.services.NavigationDropdown
import uk.ac.warwick.postroom.services.NavigationPage
import uk.ac.warwick.postroom.services.NavigationPresenter
import uk.ac.warwick.postroom.templates.FreeMarkerRendering

class NavTest : ContextTest(), FreeMarkerRendering {
  @Autowired
  override lateinit var configuration: Configuration

  @Test
  fun testRenders() {
    val template = "<#import '*/helpers/nav.ftlh' as nav>" +
      "<@nav.nav navigation />"

    val model = mapOf(
      "navigation" to NavigationPresenter(
        "/", listOf(
          NavigationPage("Home", "/"),
          NavigationDropdown(
            "Dropdown", "/dropdown", listOf(
              NavigationPage("Item one", "/dropdown/one"),
              NavigationPage("Item two", "/dropdown/two")
            )
          ),
          NavigationPage(
            "Parent page", "/parent", listOf(
              NavigationPage(
                "Child page", "/parent/child", listOf(
                  NavigationPage("Sub-child page", "/parent/child/child")
                )
              )
            )
          )
        )
      )
    )

    renderTemplateString(template, model)
  }
}
