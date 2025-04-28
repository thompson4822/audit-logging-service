package com.sthompson.auditLogging.controllers

import com.sthompson.auditLogging.dtos.CreateAuditLogRequest
import com.sthompson.auditLogging.entities.AuditStatus
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import com.sthompson.auditLogging.services.AuditLogService
import com.sthompson.auditLogging.dtos.AuditLogResponse
import com.sthompson.auditLogging.dtos.PagedAuditLogResponse
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.Instant

@QuarkusTest
class AuditLogControllerTest {

    @InjectMock
    lateinit var auditLogService: AuditLogService

    @Test
    fun testCreateAuditLog() {
        val request = CreateAuditLogRequest(
            serviceName = "test-service",
            eventType = "CREATE",
            entityType = "TestEntity",
            entityId = "test123",
            userId = "user123",
            status = AuditStatus.SUCCESS,
            message = "Test audit log"
        )

        val response = AuditLogResponse(
            id = 1L,
            timestamp = Instant.now(),
            serviceName = request.serviceName,
            eventType = request.eventType,
            entityType = request.entityType,
            entityId = request.entityId,
            userId = request.userId,
            status = request.status,
            details = null,
            message = request.message
        )

        // Mock the service
        Mockito.`when`(auditLogService.createAuditLog(request)).thenReturn(response)

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/audit-logs")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("serviceName", equalTo(request.serviceName))
            .body("eventType", equalTo(request.eventType))
            .body("entityType", equalTo(request.entityType))
            .body("entityId", equalTo(request.entityId))
            .body("userId", equalTo(request.userId))
            .body("status", equalTo(request.status.toString()))
            .body("message", equalTo(request.message))
    }

    @Test
    fun testGetAuditLogNotFound() {
        val nonExistentId = 999L

        // Mock the service to return null for a non-existent ID
        Mockito.`when`(auditLogService.getAuditLog(nonExistentId)).thenReturn(null)

        given()
            .`when`()
            .get("/audit-logs/$nonExistentId")
            .then()
            .statusCode(404)
    }

    @Test
    fun testSearchAuditLogs() {
        // Create a mock response
        val pagedResponse = PagedAuditLogResponse(
            content = emptyList(),
            pageNumber = 0,
            pageSize = 10,
            totalElements = 0,
            totalPages = 0
        )

        // Create a filter for the mock
        val filter = com.sthompson.auditLogging.dtos.AuditLogFilter()

        // Mock the service with a specific filter
        Mockito.`when`(auditLogService.searchAuditLogs(filter, 0, 10))
            .thenReturn(pagedResponse)

        given()
            .`when`()
            .get("/audit-logs?page=0&size=10")
            .then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("pageNumber", equalTo(0))
            .body("pageSize", equalTo(10))
    }
}
