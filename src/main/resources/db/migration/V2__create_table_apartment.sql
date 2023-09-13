CREATE TABLE apartment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    building_id INT,
    number INT,
    area DECIMAL(10, 2),
    FOREIGN KEY (building_id) REFERENCES building(id)
);
