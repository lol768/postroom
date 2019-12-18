package uk.ac.warwick.postroom.controllers

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import uk.ac.warwick.postroom.helpers.RomanNumerals
import uk.ac.warwick.postroom.services.NavigationPresenter
import uk.ac.warwick.postroom.services.NavigationService
import uk.ac.warwick.postroom.system.RequestContext
import uk.ac.warwick.postroom.system.security.WarwickAuthentication
import uk.ac.warwick.sso.client.SSOClientFilter
import uk.ac.warwick.sso.client.SSOConfiguration
import uk.ac.warwick.sso.client.core.LinkGeneratorImpl
import uk.ac.warwick.sso.client.core.ServletRequestAdapter
import uk.ac.warwick.userlookup.AnonymousUser
import uk.ac.warwick.userlookup.User
import java.time.LocalDate
import javax.servlet.http.HttpServletRequest

@ControllerAdvice(basePackageClasses = [HomeController::class])
class BaseControllerAdvice(
  private val navigationService: NavigationService,
  private val ssoConfiguration: SSOConfiguration
) {
  @ModelAttribute("navigation")
  fun navigation(request: HttpServletRequest, auth: Authentication?) =
    NavigationPresenter(request.servletPath, navigationService.navigation(auth))

  @ModelAttribute("requestContext")
  fun requestContext(request: HttpServletRequest, auth: Authentication?): RequestContext {
    val linkGenerator = LinkGeneratorImpl(ssoConfiguration, ServletRequestAdapter(request))

    return RequestContext(
      path = request.servletPath,
      auth = auth as? WarwickAuthentication,
      user = (request.getAttribute(SSOClientFilter.USER_KEY) as User?) ?: AnonymousUser(),
      actualUser = (request.getAttribute(SSOClientFilter.ACTUAL_USER_KEY) as User?) ?: AnonymousUser(),
      loginUrl = linkGenerator.loginUrl,
      logoutUrl = linkGenerator.logoutUrl
    )
  }

  @ModelAttribute("copyrightYear")
  fun copyrightYear() = RomanNumerals.encode(LocalDate.now().year)
}
