package io.github.hlipinski.domain

import java.time.Instant

data class Screening(
    val id: Long,
    val title: String,
    val year: Int,
    val lengthInSeconds: Int,
    val screeningTime: Instant,
    val room: String
)
