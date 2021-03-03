package io.github.hlipinski.integration.postgres

import io.github.hlipinski.adapters.postgres.ScreeningEntityTable
import io.github.hlipinski.adapters.postgres.insert
import io.github.hlipinski.adapters.postgres.toScreeningEntity
import io.github.hlipinski.domain.Screening
import io.github.hlipinski.integration.IntegrationTest
import io.github.hlipinski.integration.ScreeningControllerTest
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit.jupiter.EnabledIf
import org.springframework.transaction.annotation.Transactional

@EnabledIf(expression = "#{environment['app.main.database'] == 'postgres'}", loadContext = true)
@IntegrationTest
@Transactional
class PostgresScreeningControllerTest @Autowired private constructor(restTemplate: TestRestTemplate) :
    ScreeningControllerTest(restTemplate) {

    @Test
    fun `should return 1 screenings with both valid dates given`() {
        val screening = givenScreeningInPostgreSQLDB()

        thenScreeningReturned(screening)
    }

    private fun givenScreeningInPostgreSQLDB(): Screening {
        SchemaUtils.create(ScreeningEntityTable)
        val screening = givenScreening()
        transaction {
            ScreeningEntityTable.insert(screening.toScreeningEntity())
            commit()
        }
        return screening
    }
}