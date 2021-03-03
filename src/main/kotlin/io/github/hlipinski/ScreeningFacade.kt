package io.github.hlipinski

import io.github.hlipinski.domain.Screening
import io.github.hlipinski.domain.ScreeningRepository
import io.github.hlipinski.rest.ScreeningDto
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.text.Normalizer
import java.time.Instant

@Component
@Transactional
class ScreeningFacade internal constructor(private val repository: ScreeningRepository) {
    fun getScreenings(from: Instant?, to: Instant?): List<ScreeningDto> {
        return repository.getScreening(from ?: Instant.MIN, to ?: Instant.MAX)
            .sortWithNormalization()
            .map { it.toDto() }
    }

    fun List<Screening>.sortWithNormalization() = this.sortedBy { Normalizer.normalize(it.title, Normalizer.Form.NFD) }
}

fun Screening.toDto() = ScreeningDto(id, title, year, lengthInSeconds, screeningTime, room)
