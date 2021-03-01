package io.github.hlipinski.rest

import java.time.Instant

data class ScreeningDtoList(val screenings: List<ScreeningDto>)

data class ScreeningDto(
    val id: Long,
    val title: String,
    val year: Int,
    val lengthInSeconds: Int,
    val screeningTime: Instant,
    val room: String
)
