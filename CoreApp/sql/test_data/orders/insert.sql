/*id = 1*/ INSERT INTO orders (o_type, o_status, o_confirmation, o_timestamp, p_id) VALUES ('PICKUP', 'SUBMITTED', 'order-confirmation-123', current_timestamp(), 1);
/*id = 2*/ INSERT INTO orders (o_type, o_status, o_confirmation, o_timestamp, p_id) VALUES ('PICKUP', 'PREPARED' , 'order-confirmation-456', current_timestamp(), 1);
/*id = 3*/ INSERT INTO orders (o_type, o_status, o_confirmation, o_timestamp, p_id) VALUES ('PICKUP', 'CANCELED' , 'order-confirmation-789', current_timestamp(), 1);

/*id = 4*/ INSERT INTO orders (o_type, o_status, o_confirmation, o_timestamp, p_id, addr_id) VALUES ('DELIVERY', 'SUBMITTED', 'order-confirmation-101', current_timestamp(), 2, 6);
