package io.github.hlipinski.integration.mongo

import io.github.hlipinski.domain.Screening
import io.github.hlipinski.domain.ScreeningRepository
import io.github.hlipinski.integration.IntegrationTest
import io.github.hlipinski.integration.ScreeningControllerTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit.jupiter.EnabledIf

@EnabledIf(expression = "#{environment['app.main.database'] == 'mongodb'}", loadContext = true)
@IntegrationTest
class MongoScreeningControllerTest @Autowired private constructor(
    restTemplate: TestRestTemplate,
    private val repository: ScreeningRepository) : ScreeningControllerTest(restTemplate) {

    @Test
    fun `should return 1 screenings with both valid dates given`() {
        val screening = givenScreeningInMongoDB()

        thenScreeningReturned(screening)
    }



    private fun givenScreeningInMongoDB(): Screening {
        val screening = givenScreening()
        repository.insert(screening)
        return screening
    }
}