package io.github.hlipinski.rest

import io.github.hlipinski.ScreeningFacade
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/screenings", produces = ["application/json"])
class ScreeningController(val screeningFacade: ScreeningFacade) {

    @GetMapping()
    fun getScreenings(@RequestParam dateFrom: Instant?, @RequestParam dateTo: Instant?): ScreeningDtoList {
        return ScreeningDtoList(screeningFacade.getScreenings(dateFrom, dateTo))
    }
}