package io.github.hlipinski.adapters.mongo

import io.github.hlipinski.domain.Screening
import io.github.hlipinski.domain.ScreeningRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
@ConditionalOnProperty(prefix = "app.main", name = ["database"], havingValue = "mongodb")
class MongoScreeningRepository(private val mongoOperations: MongoOperations) : ScreeningRepository {

    override fun getScreening(from: Instant, to: Instant): List<Screening> {
        return mongoOperations.find(findQuery(from, to), ScreeningDocument::class.java)
            .map { it.toScreening() }
    }

    override fun insert(screening: Screening) {
        print("Adding $screening to MongDB")
        mongoOperations.insert(screening.toScreeningDocument())
    }

    private fun findQuery(from: Instant, to: Instant) =
        Query.query(Criteria.where("screeningTime").gte(from).lte(to))
}

fun ScreeningDocument.toScreening() =
    Screening(this.id, this.title, this.year, this.lengthInSeconds, this.screeningTime, this.room)

fun Screening.toScreeningDocument() =
    ScreeningDocument(this.id, this.title, this.year, this.lengthInSeconds, this.screeningTime, this.room)

@Document(collection = "screening")
class ScreeningDocument(
    @field:Id
    val id: Long,
    val title: String,
    val year: Int,
    val lengthInSeconds: Int,
    val screeningTime: Instant,
    val room: String
)