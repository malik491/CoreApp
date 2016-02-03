/*order id 1, 2, 3 (all have same order items)*/
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (1, 1, 1, 'NOT_READY');
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (1, 2, 1, 'NOT_READY');

INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (2, 1, 1, 'NOT_READY');
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (2, 2, 1, 'NOT_READY');

INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (3, 1, 1, 'NOT_READY');
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (3, 2, 1, 'NOT_READY');


/*order id 4*/
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (4, 1, 2, 'READY');
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (4, 3, 1, 'READY');
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (4, 6, 1, 'NOT_READY');
INSERT INTO order_items (o_id, m_item_id, o_item_qty, o_item_status) VALUES (4, 5, 1, 'NOT_READY');
