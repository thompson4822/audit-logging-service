package com.sthompson.auditLogging.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.*
import java.time.Instant
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "audit_logs", indexes = [
    Index(name = "idx_audit_timestamp", columnList = "timestamp"),
    Index(name = "idx_audit_service", columnList = "serviceName"),
    Index(name = "idx_audit_entity", columnList = "entityType, entityId"),
    Index(name = "idx_audit_user", columnList = "userId"),
    Index(name = "idx_audit_status", columnList = "status")
])
class AuditLog : PanacheEntity() {
    companion object : PanacheCompanion<AuditLog> {
        private val objectMapper = ObjectMapper()

        fun findByTimeRange(start: Instant, end: Instant) = 
            find("timestamp between ?1 and ?2", start, end).list()

        fun findByService(serviceName: String) = 
            find("serviceName", serviceName).list()

        fun findByEntity(entityType: String, entityId: String) = 
            find("entityType = ?1 and entityId = ?2", entityType, entityId).list()

        fun findByUser(userId: String) = 
            find("userId", userId).list()

        fun findByStatus(status: AuditStatus) = 
            find("status", status).list()
    }

    @Column(nullable = false)
    lateinit var timestamp: Instant

    @Column(nullable = false, length = 100)
    lateinit var serviceName: String

    @Column(nullable = false, length = 50)
    lateinit var eventType: String

    @Column(nullable = false, length = 100)
    lateinit var entityType: String

    @Column(nullable = false, length = 100)
    lateinit var entityId: String

    @Column(nullable = false, length = 100)
    lateinit var userId: String

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: AuditStatus = AuditStatus.SUCCESS

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    var details: JsonNode? = null

    @Column(length = 500)
    var message: String? = null

    @PrePersist
    fun setTimestamp() {
        if (!::timestamp.isInitialized) {
            timestamp = Instant.now()
        }
    }

    fun setDetailsFromMap(detailsMap: Map<String, Any>) {
        details = objectMapper.valueToTree(detailsMap)
    }
}

enum class AuditStatus {
    SUCCESS,
    FAILURE,
    PENDING,
    CANCELLED
}