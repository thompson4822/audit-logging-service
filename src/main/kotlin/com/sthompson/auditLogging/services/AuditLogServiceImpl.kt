package com.sthompson.auditLogging.services

import com.sthompson.auditLogging.dtos.*
import com.sthompson.auditLogging.entities.AuditLog
import com.sthompson.auditLogging.mappers.AuditLogMapper
import io.quarkus.panache.common.Page
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.Instant

@ApplicationScoped
class AuditLogServiceImpl(private val auditLogMapper: AuditLogMapper) : AuditLogService {

    @Transactional
    override fun createAuditLog(request: CreateAuditLogRequest): AuditLogResponse {
        val entity = auditLogMapper.toEntity(request)
        entity.persist()
        return auditLogMapper.toResponse(entity)
    }

    @Transactional
    override fun createAuditLogs(request: BatchCreateAuditLogRequest): List<AuditLogResponse> {
        return request.auditLogs.map { createAuditLog(it) }
    }

    override fun getAuditLog(id: Long): AuditLogResponse? {
        return AuditLog.findById(id)?.let { auditLogMapper.toResponse(it) }
    }

    override fun searchAuditLogs(filter: AuditLogFilter, page: Int, size: Int): PagedAuditLogResponse {
        var query = "1=1"
        val params = mutableListOf<Any>()
        var paramIndex = 1

        filter.startTime?.let {
            query += " and timestamp >= ?" + paramIndex++
            params.add(it)
        }
        filter.endTime?.let {
            query += " and timestamp <= ?" + paramIndex++
            params.add(it)
        }
        filter.serviceName?.let {
            query += " and serviceName = ?" + paramIndex++
            params.add(it)
        }
        filter.eventType?.let {
            query += " and eventType = ?" + paramIndex++
            params.add(it)
        }
        filter.entityType?.let {
            query += " and entityType = ?" + paramIndex++
            params.add(it)
        }
        filter.entityId?.let {
            query += " and entityId = ?" + paramIndex++
            params.add(it)
        }
        filter.userId?.let {
            query += " and userId = ?" + paramIndex++
            params.add(it)
        }
        filter.status?.let {
            query += " and status = ?" + paramIndex++
            params.add(it)
        }

        val count = AuditLog.count(query, *params.toTypedArray())
        val auditLogs = AuditLog.find(query, *params.toTypedArray())
            .page(Page.of(page, size))
            .list()
            .map { auditLogMapper.toResponse(it) }

        return PagedAuditLogResponse(
            content = auditLogs,
            pageNumber = page,
            pageSize = size,
            totalElements = count,
            totalPages = ((count + size - 1) / size).toInt()
        )
    }

    override fun getAuditLogsByService(serviceName: String, page: Int, size: Int): PagedAuditLogResponse {
        return searchAuditLogs(AuditLogFilter(serviceName = serviceName), page, size)
    }

    override fun getAuditLogsByUser(userId: String, page: Int, size: Int): PagedAuditLogResponse {
        return searchAuditLogs(AuditLogFilter(userId = userId), page, size)
    }

    override fun getAuditLogsByEntity(entityType: String, entityId: String, page: Int, size: Int): PagedAuditLogResponse {
        return searchAuditLogs(AuditLogFilter(entityType = entityType, entityId = entityId), page, size)
    }
}