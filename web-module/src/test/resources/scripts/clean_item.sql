DELETE FROM item_order WHERE item_id IN (SELECT id FROM item);
DELETE FROM item;