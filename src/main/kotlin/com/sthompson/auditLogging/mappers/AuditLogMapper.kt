package com.sthompson.auditLogging.mappers

import com.fasterxml.jackson.databind.ObjectMapper
import com.sthompson.auditLogging.dtos.AuditLogResponse
import com.sthompson.auditLogging.dtos.CreateAuditLogRequest
import com.sthompson.auditLogging.entities.AuditLog
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AuditLogMapper {
    private val objectMapper = ObjectMapper()

    fun toEntity(request: CreateAuditLogRequest): AuditLog {
        return AuditLog().apply {
            serviceName = request.serviceName
            eventType = request.eventType
            entityType = request.entityType
            entityId = request.entityId
            userId = request.userId
            status = request.status
            request.details?.let { setDetailsFromMap(it) }
            message = request.message
        }
    }

    fun toResponse(entity: AuditLog): AuditLogResponse {
        return AuditLogResponse(
            id = entity.id!!,
            timestamp = entity.timestamp,
            serviceName = entity.serviceName,
            eventType = entity.eventType,
            entityType = entity.entityType,
            entityId = entity.entityId,
            userId = entity.userId,
            status = entity.status,
            details = entity.details?.let { objectMapper.convertValue(it, Map::class.java) as Map<String, Any> },
            message = entity.message
        )
    }
}