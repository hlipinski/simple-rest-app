package io.github.hlipinski.adapters.postgres

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.Instant

interface SpringScreeningRepository : CrudRepository<ScreeningEntity, Long> {
    fun findByScreeningTimeBetween(from: Instant, to: Instant): List<ScreeningEntity>
}