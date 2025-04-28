-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

-- Sample audit log entries
INSERT INTO audit_logs (id, timestamp, serviceName, eventType, entityType, entityId, userId, status, message)
VALUES
    (1, CURRENT_TIMESTAMP, 'user-service', 'CREATE', 'User', 'user123', 'admin', 'SUCCESS', 'User account created successfully'),
    (2, CURRENT_TIMESTAMP, 'order-service', 'UPDATE', 'Order', 'order456', 'user123', 'SUCCESS', 'Order status updated to SHIPPED'),
    (3, CURRENT_TIMESTAMP, 'payment-service', 'PROCESS', 'Payment', 'payment789', 'user123', 'SUCCESS', 'Payment processed successfully'),
    (4, CURRENT_TIMESTAMP, 'auth-service', 'LOGIN', 'User', 'user123', 'user123', 'SUCCESS', 'User logged in successfully'),
    (5, CURRENT_TIMESTAMP, 'order-service', 'CREATE', 'Order', 'order789', 'user456', 'SUCCESS', 'New order created'),
    (6, CURRENT_TIMESTAMP, 'payment-service', 'PROCESS', 'Payment', 'payment101', 'user456', 'FAILURE', 'Payment declined: insufficient funds'),
    (7, CURRENT_TIMESTAMP, 'inventory-service', 'UPDATE', 'Product', 'prod123', 'admin', 'SUCCESS', 'Product stock updated'),
    (8, CURRENT_TIMESTAMP, 'user-service', 'UPDATE', 'User', 'user456', 'user456', 'SUCCESS', 'User profile updated'),
    (9, CURRENT_TIMESTAMP, 'auth-service', 'LOGOUT', 'User', 'user123', 'user123', 'SUCCESS', 'User logged out'),
    (10, CURRENT_TIMESTAMP, 'order-service', 'CANCEL', 'Order', 'order101', 'user789', 'SUCCESS', 'Order cancelled by user');

-- Sample audit logs with JSON details
INSERT INTO audit_logs (id, timestamp, serviceName, eventType, entityType, entityId, userId, status, details, message)
VALUES
    (11, CURRENT_TIMESTAMP, 'user-service', 'UPDATE', 'User', 'user123', 'user123', 'SUCCESS',
    '{"changes": {"email": "new@email.com", "phone": "+1234567890"}, "ipAddress": "192.168.1.1"}',
    'User profile updated');

INSERT INTO audit_logs (id, timestamp, serviceName, eventType, entityType, entityId, userId, status, details, message)
VALUES
    (12, CURRENT_TIMESTAMP, 'order-service', 'CREATE', 'Order', 'order202', 'user123', 'SUCCESS',
    '{"items": [{"productId": "prod123", "quantity": 2}, {"productId": "prod456", "quantity": 1}], "totalAmount": 150.00}',
    'New order created with multiple items');

-- Reset the sequence
ALTER SEQUENCE audit_logs_seq RESTART WITH 13;
