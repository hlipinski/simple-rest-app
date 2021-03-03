package io.github.hlipinski.adapters.postgres

import io.github.hlipinski.domain.Screening
import io.github.hlipinski.domain.ScreeningRepository
import org.jetbrains.exposed.sql.select
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@ConditionalOnProperty(prefix = "app.main", name = ["database"], havingValue = "postgres")
@Component
class PostgresScreeningRepository() : ScreeningRepository {
    override fun getScreening(from: Instant, to: Instant): List<Screening> =
        ScreeningEntityTable.select { ScreeningEntityTable.screeningTime.between(from, to) }
            .toScreeningEntityList()
            .map { it.toScreening() }

    override fun insert(screening: Screening) {
        print("Adding $screening to PostgreSQL database")
        ScreeningEntityTable.insert(screening.toScreeningEntity())
    }
}

fun ScreeningEntity.toScreening() =
    Screening(this.id!!, this.title, this.year, this.lengthInSeconds, this.screeningTime, this.room)

fun Screening.toScreeningEntity() =
    ScreeningEntity(this.id, this.title, this.year, this.lengthInSeconds, this.screeningTime, this.room)

@Entity
data class ScreeningEntity (
    @Id @GeneratedValue
    val id: Long? = null,
    val title: String,
    val year: Int,
    val lengthInSeconds: Int,
    val screeningTime: Instant,
    val room: String
)
