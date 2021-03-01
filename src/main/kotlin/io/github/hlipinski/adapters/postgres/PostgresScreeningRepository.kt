package io.github.hlipinski.adapters.postgres

import io.github.hlipinski.domain.Screening
import io.github.hlipinski.domain.ScreeningRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.Id

@ConditionalOnProperty(prefix = "app.main", name = ["database"], havingValue = "postgres")
@Component
class PostgresScreeningRepository(private val repository: SpringScreeningRepository) : ScreeningRepository {
    override fun getScreening(from: Instant, to: Instant): List<Screening> =
        repository.findByScreeningTimeBetween(from, to)
            .map { it.toScreening() }

    override fun insert(screening: Screening) {
        print("Adding $screening to PostgreSQL database")
        repository.save(screening.toScreeningEntity())
    }
}

fun ScreeningEntity.toScreening() =
    Screening(this.id!!, this.title!!, this.year!!, this.lengthInSeconds!!, this.screeningTime!!, this.room!!)

fun Screening.toScreeningEntity() =
    ScreeningEntity(this.id, this.title, this.year, this.lengthInSeconds, this.screeningTime, this.room)

@Entity
data class ScreeningEntity (
    @Id
    val id: Long? = null,
    val title: String? = null,
    val year: Int? = null,
    val lengthInSeconds: Int? = null,
    val screeningTime: Instant? = null,
    val room: String? = null
)