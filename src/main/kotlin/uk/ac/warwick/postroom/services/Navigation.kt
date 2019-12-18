package uk.ac.warwick.postroom.services

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import uk.ac.warwick.postroom.controllers.HomeController
import uk.ac.warwick.postroom.controllers.MasqueradeController
import uk.ac.warwick.postroom.controllers.SysadminController
import uk.ac.warwick.postroom.system.security.Authority
import kotlin.reflect.KClass

interface NavigationService {
  fun navigation(authentication: Authentication?): List<NavigationItem>
}

@Service
class NavigationServiceImpl : NavigationService {
  override fun navigation(authentication: Authentication?): List<NavigationItem> {
    fun hasAuthority(authority: GrantedAuthority): Boolean =
      authentication?.authorities?.contains(authority) == true

    val items: MutableList<NavigationItem> = mutableListOf()

    if (hasAuthority(Authority.user))
      items += NavigationPage("Home", path(on(HomeController::class).home()))

    if (hasAuthority(Authority.masquerader))
      items += NavigationPage("Masquerade", path(on(MasqueradeController::class).form()))

    if (hasAuthority(Authority.sysadmin))
      items += NavigationPage("Sysadmin", path(on(SysadminController::class).home()))

    return items
  }

  private fun <T : Any> on(kClass: KClass<T>) = MvcUriComponentsBuilder.on(kClass.java)

  private fun path(methodCall: Any): String = MvcUriComponentsBuilder.fromMethodCall(methodCall).build().toUriString()
}

data class NavigationPresenter(val path: String, val items: List<NavigationItem>) {
  val primaryActive: Boolean =
    path != "/" && items.any { it.isActive(path) }

  val secondary: List<NavigationItem> =
    items.filter { it.isActive(path) && path != "" }

  val tertiary: List<NavigationItem> =
    items.filter { it.isActive(path) }.mapNotNull { it.deepestActive(path) }
}

interface NavigationItem {
  val label: String
  val route: String
  val children: List<NavigationItem>
  val dropdown: Boolean

  val hasChildren: Boolean
    get() = children.isNotEmpty()

  fun isActive(path: String): Boolean =
    path.startsWith(route) || hasActiveChildren(path)

  fun hasActiveChildren(path: String) = children.any { it.isActive(path) }

  fun activeChildren(path: String) = children.filter { it.isActive(path) }

  fun deepestActive(path: String): NavigationItem? =
    if (path.startsWith(route) && children.none { it.isActive(path) }) this
    else children.map { it.deepestActive(path) }.firstOrNull()
}

data class NavigationPage(
  override val label: String,
  override val route: String,
  override val children: List<NavigationItem> = emptyList()
) : NavigationItem {
  override val dropdown: Boolean = false
}

data class NavigationDropdown(
  override val label: String,
  override val route: String,
  override val children: List<NavigationItem> = emptyList()
) : NavigationItem {
  override val dropdown: Boolean = true
}

