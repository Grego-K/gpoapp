CREATE TABLE capabilities (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL UNIQUE,
  description varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE categories (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  description varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE regions (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE roles (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE roles_capabilities (
  role_id bigint NOT NULL,
  capability_id bigint NOT NULL,
  PRIMARY KEY (role_id, capability_id),
  CONSTRAINT fk_capability FOREIGN KEY (capability_id) REFERENCES capabilities (id) ON DELETE CASCADE,
  CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE suppliers (
  id bigint NOT NULL AUTO_INCREMENT,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) DEFAULT NULL,
  company_name varchar(255) NOT NULL,
  email varchar(255) NOT NULL UNIQUE,
  phone_number varchar(10) DEFAULT NULL,
  uuid varchar(255) DEFAULT NULL UNIQUE,
  vat varchar(9) NOT NULL UNIQUE,
  region_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_supplier_region FOREIGN KEY (region_id) REFERENCES regions (id)
);

CREATE TABLE users (
  id bigint NOT NULL AUTO_INCREMENT,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) DEFAULT NULL,
  email varchar(255) NOT NULL UNIQUE,
  firstname varchar(255) NOT NULL,
  lastname varchar(255) NOT NULL,
  phone_number varchar(10) DEFAULT NULL,
  uuid varchar(255) DEFAULT NULL UNIQUE,
  vat varchar(9) NOT NULL UNIQUE,
  region_id bigint DEFAULT NULL,
  password varchar(255) NOT NULL,
  username varchar(255) NOT NULL UNIQUE,
  role_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles (id),
  CONSTRAINT FK_user_region FOREIGN KEY (region_id) REFERENCES regions (id)
);

CREATE TABLE products (
  id bigint NOT NULL AUTO_INCREMENT,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) DEFAULT NULL,
  base_price decimal(10,2) NOT NULL,
  description varchar(500) DEFAULT NULL,
  gpo_price decimal(10,2) NOT NULL,
  product_name varchar(255) NOT NULL,
  stock_quantity int NOT NULL,
  uuid varchar(255) NOT NULL UNIQUE,
  supplier_id bigint DEFAULT NULL,
  category_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_product_category FOREIGN KEY (category_id) REFERENCES categories (id),
  CONSTRAINT FK_product_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers (id)
);

CREATE TABLE orders (
  id bigint NOT NULL AUTO_INCREMENT,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) DEFAULT NULL,
  status varchar(50) DEFAULT NULL,
  uuid varchar(255) NOT NULL UNIQUE,
  user_id bigint NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_order_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE order_items (
  id bigint NOT NULL AUTO_INCREMENT,
  order_id bigint NOT NULL,
  product_id bigint NOT NULL,
  quantity int NOT NULL,
  unit_price decimal(10,2) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_order_items_order FOREIGN KEY (order_id) REFERENCES orders (id),
  CONSTRAINT FK_order_items_product FOREIGN KEY (product_id) REFERENCES products (id)
);