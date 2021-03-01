package io.github.hlipinski.integration

import io.github.hlipinski.domain.Screening
import io.github.hlipinski.domain.ScreeningRepository
import io.github.hlipinski.rest.ScreeningDtoList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.Instant

@IntegrationTest
class ScreeningControllerTest @Autowired private constructor(
    private val restTemplate: TestRestTemplate,
    private val repository: ScreeningRepository
) {
    @Test
    fun `should return 1 screenings with both valid dates given`() {
        val screeningDocument = givenScreeningInDB()

        val response = restTemplate.getForObject(
            "/screenings?dateFrom=${screeningDocument.screeningTime.minusSeconds(1)}&dateTo=${
                screeningDocument.screeningTime.plusSeconds(1)
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
        repository.insert(screening)
        return screening
    }
}