CREATE TABLE addresses (
	addr_id					BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	
	addr_line1				VARCHAR(100) NOT NULL,
	addr_line2				VARCHAR(100) DEFAULT '',
	addr_city				VARCHAR(30) NOT NULL,
	addr_state				ENUM ('AL','AK', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DE', 'FL', 'GA', 'HI', 'ID',
							  'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD', 'MA', 'MI', 'MN', 'MS',
							  'MO', 'MT', 'NE', 'NV', 'NH', 'NJ', 'NM', 'NY', 'NC', 'ND', 'OH', 'OK',
							  'OR', 'PA', 'RI', 'SC', 'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 
							  'WI', 'WY') NOT NULL,
	addr_zipcode			VARCHAR(10) NOT NULL,

	PRIMARY KEY (addr_id)
);

CREATE TABLE users (
	u_id				BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	
	u_first_name		VARCHAR(20) NOT NULL,
	u_last_name			VARCHAR(20) NOT NULL,
    u_email				VARCHAR(30) NOT NULL,	
	u_phone				VARCHAR(15) DEFAULT '',
	addr_id				BIGINT UNSIGNED NOT NULL,
	
	PRIMARY KEY (u_id),
	FOREIGN KEY (addr_id) REFERENCES addresses (addr_id)
);

CREATE TABLE accounts (
	acc_username			VARCHAR(30) NOT NULL,
	acc_password			VARCHAR(60) NOT NULL,
	acc_role				enum ('ADMIN', 'MANAGER', 'EMPLOYEE', 'VENDOR', 'CUSTOMER_APP') NOT NULL,
	
	u_id					BIGINT UNSIGNED NOT NULL,
	
	PRIMARY KEY (acc_username),
	FOREIGN KEY (u_id) REFERENCES users (u_id)
);

CREATE TABLE payments (
	p_id						BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    
    p_type						ENUM ('CASH', 'CREDIT_CARD') NOT NULL,
    p_total  					DECIMAL(7, 2) NOT NULL,
    p_cc_transaction_confm   	VARCHAR(50) NOT NULL,
    
    PRIMARY KEY(p_id)
);

CREATE TABLE orders (
	o_id       				BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    
    o_type					ENUM ('PICKUP', 'DELIVERY') NOT NULL,
	o_status				ENUM ('SUBMITTED', 'PREPARED', 'CANCELED') NOT NULL,
	o_confirmation  		VARCHAR(50) NOT NULL,
	o_timestamp				TIMESTAMP NOT NULL,
	p_id					BIGINT UNSIGNED NOT NULL,

	o_notification_email 	VARCHAR(50) DEFAULT NULL,
	addr_id    				BIGINT UNSIGNED DEFAULT NULL,
    
	PRIMARY KEY (o_id),
	FOREIGN KEY (addr_id) REFERENCES addresses (addr_id),
	FOREIGN KEY (p_id) REFERENCES payments (p_id)

);

CREATE TABLE menu_items (
	m_item_id			BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	
	m_item_name			VARCHAR(100) NOT NULL,
	m_item_desc			VARCHAR(300) DEFAULT '',
	m_item_price		DECIMAL(5, 2) DEFAULT 0,
	m_item_category		ENUM ('BEVERAGE', 'MAIN', 'SIDE') NOT NULL,
    
	PRIMARY KEY (m_item_id)
);

CREATE TABLE order_items (
	o_id            BIGINT UNSIGNED NOT NULL,
	m_item_id		BIGINT UNSIGNED NOT NULL,
	
	o_item_qty		SMALLINT UNSIGNED NOT NULL,
	o_item_status   ENUM ('READY', 'NOT_READY') NOT NULL DEFAULT 'NOT_READY',
    
	PRIMARY KEY (o_id, m_item_id),
	FOREIGN KEY (o_id) REFERENCES orders (o_id),
	FOREIGN KEY (m_item_id) REFERENCES menu_items (m_item_id)
);

CREATE TABLE recipes (
	recipe_id           BIGINT UNSIGNED NOT NULL,
	m_item_id			BIGINT UNSIGNED NOT NULL,
	
	recipe_desc			VARCHAR(300) NOT NULL DEFAULT '',
	
	PRIMARY KEY (recipe_id),
	FOREIGN KEY (m_item_id) REFERENCES menu_items (m_item_id)
);

CREATE TABLE inventory_items (
	inv_item_id					BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    
    inv_item_qty				INT UNSIGNED NOT NULL,
    inv_item_measurement_unit 	ENUM ('LB','OZ', 'MG', 'G', 'PIECE') NOT NULL,
	
    PRIMARY KEY (inv_item_id)
);

CREATE TABLE recipe_items (
	recipe_id      			BIGINT UNSIGNED NOT NULL,
	inv_item_id				BIGINT UNSIGNED NOT NULL,
	
	r_item_qty				SMALLINT unsigned NOT NULL,
	r_item_measurement_unit ENUM ('LB','OZ', 'MG', 'G', 'PIECE') NOT NULL,
    
	PRIMARY KEY (recipe_id, inv_item_id),
	FOREIGN KEY (recipe_id) REFERENCES recipes (recipe_id),
	FOREIGN KEY (inv_item_id) REFERENCES inventory_items (inv_item_id)
);


CREATE TABLE emails (
	email_id			BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    
    email_to			VARCHAR(30) NOT NULL,
    email_from			VARCHAR(30) NOT NULL,
	email_subject		VARCHAR(100) NOT NULL,
    email_text			VARCHAR(300) NOT NULL,
    email_sent_status   TINYINT NOT NULL DEFAULT 0,	
    
    PRIMARY KEY (email_id)
);


