package uk.ac.warwick.postroom.mvc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import uk.ac.warwick.postroom.context.ContextTest

@AutoConfigureMockMvc
abstract class MvcTest : ContextTest() {
  @Autowired
  lateinit var mvc: MockMvc
}
