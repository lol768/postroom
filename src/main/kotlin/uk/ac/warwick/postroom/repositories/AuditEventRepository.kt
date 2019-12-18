package uk.ac.warwick.postroom.repositories

import org.springframework.data.repository.CrudRepository
import uk.ac.warwick.postroom.entities.AuditEvent
import java.util.*

interface AuditEventRepository : CrudRepository<AuditEvent, UUID>
