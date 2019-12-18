package uk.ac.warwick.postroom.context

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@RunWith(SpringRunner::class)
@SpringBootTest
abstract class ContextTest
