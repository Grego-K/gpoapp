-- Αντηλιακά & Κρέμες
INSERT INTO rebate_tiers (product_id, min_quantity, max_quantity, rebate_amount, period_type)
SELECT id, 0, 50, 0.00, 'QUARTER' FROM products WHERE category_id IN (2, 3, 4, 5);
INSERT INTO rebate_tiers (product_id, min_quantity, max_quantity, rebate_amount, period_type)
SELECT id, 51, 150, 2.00, 'QUARTER' FROM products WHERE category_id IN (2, 3, 4, 5);
INSERT INTO rebate_tiers (product_id, min_quantity, max_quantity, rebate_amount, period_type)
SELECT id, 151, 999999, 4.00, 'QUARTER' FROM products WHERE category_id IN (2, 3, 4, 5);

-- Βιταμίνες & Αρώματα
INSERT INTO rebate_tiers (product_id, min_quantity, max_quantity, rebate_amount, period_type)
SELECT id, 0, 40, 0.00, 'QUARTER' FROM products WHERE category_id IN (9, 10);
INSERT INTO rebate_tiers (product_id, min_quantity, max_quantity, rebate_amount, period_type)
SELECT id, 41, 120, 3.00, 'QUARTER' FROM products WHERE category_id IN (9, 10);
INSERT INTO rebate_tiers (product_id, min_quantity, max_quantity, rebate_amount, period_type)
SELECT id, 121, 999999, 5.00, 'QUARTER' FROM products WHERE category_id IN (9, 10);

-- Παυσίπονα & Σαπούνια
INSERT INTO rebate_tiers (product_id, min_quantity, max_quantity, rebate_amount, period_type)
SELECT id, 0, 100, 0.00, 'QUARTER' FROM products WHERE category_id IN (6, 8);
INSERT INTO rebate_tiers (product_id, min_quantity, max_quantity, rebate_amount, period_type)
SELECT id, 101, 300, 1.00, 'QUARTER' FROM products WHERE category_id IN (6, 8);
INSERT INTO rebate_tiers (product_id, min_quantity, max_quantity, rebate_amount, period_type)
SELECT id, 301, 999999, 2.00, 'QUARTER' FROM products WHERE category_id IN (6, 8);

-- Μάσκες & Ορθοπεδικά
INSERT INTO rebate_tiers (product_id, min_quantity, max_quantity, rebate_amount, period_type)
SELECT id, 0, 30, 0.00, 'QUARTER' FROM products WHERE category_id IN (1, 7);
INSERT INTO rebate_tiers (product_id, min_quantity, max_quantity, rebate_amount, period_type)
SELECT id, 31, 100, 4.00, 'QUARTER' FROM products WHERE category_id IN (1, 7);
INSERT INTO rebate_tiers (product_id, min_quantity, max_quantity, rebate_amount, period_type)
SELECT id, 101, 999999, 6.00, 'QUARTER' FROM products WHERE category_id IN (1, 7);

-- ΑΡΧΙΚΟΠΟΙΗΣΗ PROGRESS ΓΙΑ ΟΛΑ ΤΑ ΠΡΟΪΟΝΤΑ
INSERT INTO product_progress (product_id, volume, period_label, period_type)
SELECT id, 0, '2026-Q1', 'QUARTER' FROM products;