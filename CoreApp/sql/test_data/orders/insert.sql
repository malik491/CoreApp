/*id = 1*/ INSERT INTO orders (o_type, o_status, o_confirmation, o_timestamp, p_id) 
							   VALUES ('PICKUP', 'SUBMITTED', 'order-confirmation-123', current_timestamp(), 1);
/*id = 2*/ INSERT INTO orders (o_type, o_status, o_confirmation, o_timestamp, p_id, addr_id)  
							   VALUES ('DELIVERY', 'SUBMITTED', 'order-confirmation-456', current_timestamp(), 2, 6);