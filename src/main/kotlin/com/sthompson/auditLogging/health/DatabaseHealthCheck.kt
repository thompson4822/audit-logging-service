package com.sthompson.auditLogging.health

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.health.HealthCheck
import org.eclipse.microprofile.health.HealthCheckResponse
import org.eclipse.microprofile.health.Readiness
import jakarta.inject.Inject
import javax.sql.DataSource

@ApplicationScoped
@Readiness
class DatabaseHealthCheck : HealthCheck {

    @Inject
    lateinit var dataSource: DataSource

    override fun call(): HealthCheckResponse {
        val builder = HealthCheckResponse.builder().name("Database connection health check")

        try {
            dataSource.connection.use { connection ->
                if (connection.isValid(5)) {
                    builder.up()
                } else {
                    builder.down().withData("error", "Database connection is not valid")
                }
            }
        } catch (e: Exception) {
            builder.down().withData("error", e.message ?: "Unknown error")
        }

        return builder.build()
    }
}
