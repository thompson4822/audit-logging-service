package com.sthompson.auditLogging.dtos

import com.sthompson.auditLogging.entities.AuditStatus
import java.time.Instant

data class CreateAuditLogRequest(
    val serviceName: String,
    val eventType: String,
    val entityType: String,
    val entityId: String,
    val userId: String,
    val status: AuditStatus = AuditStatus.SUCCESS,
    val details: Map<String, Any>? = null,
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