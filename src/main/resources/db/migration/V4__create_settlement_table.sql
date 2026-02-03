CREATE TABLE rebate_settlements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    period_label VARCHAR(20) NOT NULL,
    total_rebate_amount DECIMAL(19, 2) NOT NULL,
    settled_at TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_settlement_user FOREIGN KEY (user_id) REFERENCES users(id)
);