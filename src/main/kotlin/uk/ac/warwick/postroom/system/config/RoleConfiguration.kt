package uk.ac.warwick.postroom.system.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("postroom.roles")
class RoleConfiguration {
  lateinit var adminWebgroup: String
  lateinit var sysadminWebgroup: String
  lateinit var masqueradersWebgroup: String
}
