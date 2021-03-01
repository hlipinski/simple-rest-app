package io.github.hlipinski

import io.github.hlipinski.domain.Screening
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant

internal class ScreeningFacadeTest {

    private val screeningRepository = InMemoryScreeningRepository()
    private val facade = ScreeningFacade(screeningRepository)

    @Test
    fun `should return screenings with both valid dates given`() {
        val screening = givenScreeningInDB()

        val screeningsList =
            facade.getScreenings(screening.screeningTime.minusMillis(1), screening.screeningTime.plusMillis(1))

        assertThat(screeningsList).isNotEmpty
        assertThat(screeningsList.size).isEqualTo(1)
    }

    @Test
    fun `should return screenings with only from date given`() {
        val screening = givenScreeningInDB()

        val screeningsList = facade.getScreenings(screening.screeningTime.minusMillis(1), null)

        assertThat(screeningsList).isNotEmpty
        assertThat(screeningsList.size).isEqualTo(1)
    }

    @Test
    fun `should return screenings with only to date given`() {
        val screening = givenScreeningInDB()

        val screeningsList = facade.getScreenings(null, screening.screeningTime.plusMillis(1))

        assertThat(screeningsList).isNotEmpty
        assertThat(screeningsList.size).isEqualTo(1)
    }

    @Test
    fun `should return screenings with no dates given`() {
        givenScreeningInDB()

        val screeningsList = facade.getScreenings(null, null)

        assertThat(screeningsList).isNotEmpty
        assertThat(screeningsList.size).isEqualTo(1)
    }

    @Test
    fun `should return no screenings`() {
        givenScreeningInDB()

        val screeningsList = facade.getScreenings(Instant.MIN, Instant.MIN)

        assertThat(screeningsList).isEmpty()
    }

    @Test
    fun `should return screenings in given order`() {
        givenScreeningInDB(title = "alamakota")
        givenScreeningInDB(title = "ścigany")
        givenScreeningInDB(title = "zemsta")

        val screeningsList = facade.getScreenings(null, null)

        assertThat(screeningsList).isNotEmpty
        assertThat(screeningsList.size).isEqualTo(3)
        assertThat(screeningsList[0].title).isEqualTo("alamakota")
        assertThat(screeningsList[2].title).isEqualTo("zemsta")
    }

    private fun givenScreeningInDB(
        title: String = "ścigany",
        year: Int = 2002,
        lengthInSeconds: Int = 3600,
        screeningTime: Instant = Instant.now(),
        room: String = "redrum"
    ): Screening {
        val screening = Screening(1L, title, year, lengthInSeconds, screeningTime, room)
        screeningRepository.insert(screening)
        return screening
    }
}