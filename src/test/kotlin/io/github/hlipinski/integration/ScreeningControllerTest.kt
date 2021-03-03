package io.github.hlipinski.integration

import io.github.hlipinski.adapters.postgres.ScreeningEntityTable
import io.github.hlipinski.adapters.postgres.insert
import io.github.hlipinski.adapters.postgres.toScreeningEntity
import io.github.hlipinski.adapters.postgres.toScreeningEntityList
import io.github.hlipinski.domain.Screening
import io.github.hlipinski.domain.ScreeningRepository
import io.github.hlipinski.rest.ScreeningDtoList
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@IntegrationTest
@Transactional
class ScreeningControllerTest
@Autowired private constructor(private val restTemplate: TestRestTemplate) {

    @BeforeEach
    internal fun setUp() {
        SchemaUtils.create(ScreeningEntityTable)
    }

    @Test
    fun `should return 1 screenings with both valid dates given`() {
        val screening = givenScreeningInDB()

        val response = restTemplate.getForObject(
            "/screenings?dateFrom=${screening.screeningTime.minusSeconds(1)}&dateTo=${
                screening.screeningTime.plusSeconds(1)
            }",
            ScreeningDtoList::class.java
        )

        assertThat(response.screenings).isNotEmpty
        assertThat(response.screenings.size).isEqualTo(1)
    }

    private fun givenScreeningInDB(
        title: String = "ścigany",
        year: Int = 2002,
        lengthInSeconds: Int = 3600,
        screeningTime: Instant = Instant.now(),
        room: String = "redrum"
    ): Screening {
        val screening = Screening(1L, title, year, lengthInSeconds, screeningTime, room)
        transaction {
            ScreeningEntityTable.insert(screening.toScreeningEntity())
            commit()
        }
        return screening
    }
}
