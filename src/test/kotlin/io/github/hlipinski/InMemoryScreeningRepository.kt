package io.github.hlipinski

import io.github.hlipinski.domain.Screening
import io.github.hlipinski.domain.ScreeningRepository
import java.time.Instant

class InMemoryScreeningRepository : ScreeningRepository {
    private val database: HashSet<Screening> = hashSetOf()

    override fun insert(screening: Screening) {
        database.add(screening)
    }

    override fun getScreening(from: Instant, to: Instant): List<Screening> {
        return database.filter { !from.isAfter(it.screeningTime) && !to.isBefore(it.screeningTime) }
    }
}