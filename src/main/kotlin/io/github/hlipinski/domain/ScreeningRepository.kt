package io.github.hlipinski.domain

import java.time.Instant

internal interface ScreeningRepository {
    fun getScreening(from: Instant, to: Instant): List<Screening>
    fun insert(screening: Screening)
}