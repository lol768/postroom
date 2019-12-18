package uk.ac.warwick.postroom.system.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("audit")
class AuditConfiguration {
  lateinit var applicationKey: String
}
