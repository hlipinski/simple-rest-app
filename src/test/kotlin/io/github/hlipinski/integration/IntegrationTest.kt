package io.github.hlipinski.integration

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [MongoDBInitializer::class, PostgresDBInitializer::class])
@ExtendWith(SpringExtension::class)
@ActiveProfiles("it")
annotation class IntegrationTest

// https://dev.to/sivalabs/springboot-integration-testing-using-testcontainers-starter-13h2
internal class MongoDBInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    private val mongoContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.23-xenial"))

    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        mongoContainer.start()

        TestPropertyValues.of(
            "spring.data.mongodb.host=" + mongoContainer.host,
            "spring.data.mongodb.port=" + mongoContainer.firstMappedPort
        ).applyTo(configurableApplicationContext.environment)
    }
}

internal class PostgresDBInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    private val postgresContainer = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13.2"))

    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        postgresContainer.start()

        TestPropertyValues.of(
            "spring.datasource.url=" + postgresContainer.jdbcUrl,
            "spring.datasource.username=" + postgresContainer.username,
            "spring.datasource.password=" + postgresContainer.password
        ).applyTo(configurableApplicationContext.environment);
    }
}
