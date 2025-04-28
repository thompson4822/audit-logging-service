package com.sthompson.auditLogging.services

import com.sthompson.auditLogging.dtos.*

interface AuditLogService {
    fun createAuditLog(request: CreateAuditLogRequest): AuditLogResponse
    fun createAuditLogs(request: BatchCreateAuditLogRequest): List<AuditLogResponse>
    fun getAuditLog(id: Long): AuditLogResponse?
    fun searchAuditLogs(filter: AuditLogFilter, page: Int, size: Int): PagedAuditLogResponse
    fun getAuditLogsByService(serviceName: String, page: Int, size: Int): PagedAuditLogResponse
    fun getAuditLogsByUser(userId: String, page: Int, size: Int): PagedAuditLogResponse
    fun getAuditLogsByEntity(entityType: String, entityId: String, page: Int, size: Int): PagedAuditLogResponse
}