/*order id 1*/
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (1, 1, 1, 'NOT_READY');
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (1, 2, 1, 'NOT_READY');

/*order id 2*/
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (2, 1, 2, 'READY');
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (2, 3, 1, 'READY');
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (2, 6, 1, 'NOT_READY');
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (2, 5, 1, 'NOT_READY');