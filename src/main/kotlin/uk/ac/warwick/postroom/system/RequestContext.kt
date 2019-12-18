package uk.ac.warwick.postroom.system

import uk.ac.warwick.postroom.system.security.WarwickAuthentication
import uk.ac.warwick.userlookup.User

data class RequestContext(
  val path: String,
  val auth: WarwickAuthentication?,
  val user: User,
  val actualUser: User,
  val loginUrl: String,
  val logoutUrl: String
) {
  val masquerading: Boolean = user != actualUser
}
