package io.github.hlipinski.integration

import io.github.hlipinski.domain.Screening
import io.github.hlipinski.rest.ScreeningDtoList
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.web.client.TestRestTemplate
import java.time.Instant

open class ScreeningControllerTest(private val restTemplate: TestRestTemplate) {

    internal fun thenScreeningReturned(screening: Screening) {
        val response = restTemplate.getForObject(
            "/screenings?dateFrom=${screening.screeningTime.minusSeconds(1)}&dateTo=${
                screening.screeningTime.plusSeconds(1)
            }",
            ScreeningDtoList::class.java
        )

        assertThat(response.screenings).isNotEmpty
        assertThat(response.screenings.size).isEqualTo(1)
        assertThat(response.screenings[0])
            .usingRecursiveComparison()
            .isEqualTo(screening)
    }

    internal fun givenScreening(
        title: String = "ścigany",
        year: Int = 2002,
        lengthInSeconds: Int = 3600,
        screeningTime: Instant = Instant.now(),
        room: String = "redrum"
    ): Screening = Screening(1L, title, year, lengthInSeconds, screeningTime, room)
}
