package com.sthompson.auditLogging.controllers

import com.sthompson.auditLogging.dtos.*
import com.sthompson.auditLogging.entities.AuditStatus
import com.sthompson.auditLogging.services.AuditLogService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.time.Instant

@Path("/audit-logs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
class AuditLogController(private val auditLogService: AuditLogService) {

    @POST
    fun createAuditLog(@Valid request: CreateAuditLogRequest): Response {
        val response = auditLogService.createAuditLog(request)
        return Response.status(Response.Status.CREATED)
            .entity(response)
            .build()
    }

    @POST
    @Path("/batch")
    fun createAuditLogs(@Valid request: BatchCreateAuditLogRequest): Response {
        val responses = auditLogService.createAuditLogs(request)
        return Response.status(Response.Status.CREATED)
            .entity(responses)
            .build()
    }

    @GET
    @Path("/{id}")
    fun getAuditLog(@PathParam("id") id: Long): Response {
        return auditLogService.getAuditLog(id)?.let {
            Response.ok(it).build()
        } ?: Response.status(Response.Status.NOT_FOUND).build()
    }

    @GET
    fun searchAuditLogs(
        @QueryParam("startTime") startTime: Instant?,
        @QueryParam("endTime") endTime: Instant?,
        @QueryParam("serviceName") serviceName: String?,
        @QueryParam("eventType") eventType: String?,
        @QueryParam("entityType") entityType: String?,
        @QueryParam("entityId") entityId: String?,
        @QueryParam("userId") userId: String?,
        @QueryParam("status") status: AuditStatus?,
        @DefaultValue("0") @QueryParam("page") page: Int,
        @DefaultValue("20") @QueryParam("size") size: Int
    ): Response {
        val filter = AuditLogFilter(
            startTime = startTime,
            endTime = endTime,
            serviceName = serviceName,
            eventType = eventType,
            entityType = entityType,
            entityId = entityId,
            userId = userId,
            status = status
        )

        val response = auditLogService.searchAuditLogs(filter, page, size)
        return Response.ok(response).build()
    }

    @GET
    @Path("/services/{serviceName}")
    fun getAuditLogsByService(
        @PathParam("serviceName") serviceName: String,
        @DefaultValue("0") @QueryParam("page") page: Int,
        @DefaultValue("20") @QueryParam("size") size: Int
    ): Response {
        val response = auditLogService.getAuditLogsByService(serviceName, page, size)
        return Response.ok(response).build()
    }

    @GET
    @Path("/users/{userId}")
    fun getAuditLogsByUser(
        @PathParam("userId") userId: String,
        @DefaultValue("0") @QueryParam("page") page: Int,
        @DefaultValue("20") @QueryParam("size") size: Int
    ): Response {
        val response = auditLogService.getAuditLogsByUser(userId, page, size)
        return Response.ok(response).build()
    }

    @GET
    @Path("/entities/{entityType}/{entityId}")
    fun getAuditLogsByEntity(
        @PathParam("entityType") entityType: String,
        @PathParam("entityId") entityId: String,
        @DefaultValue("0") @QueryParam("page") page: Int,
        @DefaultValue("20") @QueryParam("size") size: Int
    ): Response {
        val response = auditLogService.getAuditLogsByEntity(entityType, entityId, page, size)
        return Response.ok(response).build()
    }
}