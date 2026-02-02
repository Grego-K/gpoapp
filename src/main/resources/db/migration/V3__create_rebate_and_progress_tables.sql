CREATE TABLE rebate_tiers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    min_quantity INT NOT NULL,
    max_quantity INT NOT NULL,
    rebate_amount DECIMAL(10,2) NOT NULL,
    period_type VARCHAR(20) NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT fk_rebate_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE product_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    volume INT NOT NULL DEFAULT 0,
    period_label VARCHAR(50) NOT NULL,
    period_type VARCHAR(20) NOT NULL,
    CONSTRAINT fk_progress_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT uk_product_period UNIQUE (product_id, period_label)
);