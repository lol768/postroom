package uk.ac.warwick.postroom.system.context

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uk.ac.warwick.postroom.system.config.AuditConfiguration
import uk.ac.warwick.util.logging.AuditLogger

@Configuration
class AuditContext(
  private val config: AuditConfiguration
) {
  @Bean
  fun auditLogger(): AuditLogger = AuditLogger.getAuditLogger(config.applicationKey)
}
