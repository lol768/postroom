package uk.ac.warwick.postroom.mvc

import org.hamcrest.CoreMatchers.containsString
import org.junit.Test
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.get
import uk.ac.warwick.postroom.system.security.Role

class HomeMvcTest : MvcTest() {
  @Test
  @WithMockUser(roles = [Role.user])
  fun homeSignedIn() {
    mvc.get("/").andExpect {
      status { isOk }
      content { string(containsString("Greetings!")) }
    }
  }

  @Test
  @WithAnonymousUser
  fun homeSignedOut() {
    mvc.get("/").andExpect {
      status { isForbidden }
    }
  }
}
