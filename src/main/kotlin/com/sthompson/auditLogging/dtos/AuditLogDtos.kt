package com.sthompson.auditLogging.dtos

import com.sthompson.auditLogging.entities.AuditStatus
import java.time.Instant
import jakarta.validation.constraints.*

data class CreateAuditLogRequest(
    @field:NotBlank(message = "Service name is required")
    @field:Size(max = 100, message = "Service name cannot exceed 100 characters")
    val serviceName: String,

    @field:NotBlank(message = "Event type is required")
    @field:Size(max = 50, message = "Event type cannot exceed 50 characters")
    val eventType: String,

    @field:NotBlank(message = "Entity type is required")
    @field:Size(max = 100, message = "Entity type cannot exceed 100 characters")
    val entityType: String,

    @field:NotBlank(message = "Entity ID is required")
    @field:Size(max = 100, message = "Entity ID cannot exceed 100 characters")
    val entityId: String,

    @field:NotBlank(message = "User ID is required")
    @field:Size(max = 100, message = "User ID cannot exceed 100 characters")
    val userId: String,

    val status: AuditStatus = AuditStatus.SUCCESS,

    val details: Map<String, Any>? = null,

    @field:Size(max = 500, message = "Message cannot exceed 500 characters")
    val message: String? = null
)

data class AuditLogResponse(
    val id: Long,
    val timestamp: Instant,
    val serviceName: String,
    val eventType: String,
    val entityType: String,
    val entityId: String,
    val userId: String,
    val status: AuditStatus,
    val details: Map<String, Any>?,
    val message: String?
)

data class AuditLogFilter(
    val startTime: Instant? = null,
    val endTime: Instant? = null,
    val serviceName: String? = null,
    val eventType: String? = null,
    val entityType: String? = null,
    val entityId: String? = null,
    val userId: String? = null,
    val status: AuditStatus? = null
)

data class PagedAuditLogResponse(
    val content: List<AuditLogResponse>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int
)

data class BatchCreateAuditLogRequest(
    val auditLogs: List<CreateAuditLogRequest>
) {
    init {
        require(auditLogs.isNotEmpty()) { "Audit logs list cannot be empty" }
        require(auditLogs.size <= 1000) { "Cannot create more than 1000 audit logs in a single request" }
    }
}