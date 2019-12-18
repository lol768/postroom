package uk.ac.warwick.postroom.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.postroom.system.security.Role
import javax.annotation.security.RolesAllowed

@Controller
@RequestMapping("/")
@RolesAllowed(Role.user)
class HomeController {
  @GetMapping
  fun home() = ModelAndView("home")
}
