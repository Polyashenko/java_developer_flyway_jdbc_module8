CREATE TABLE aparnment_ownership (
    id INT AUTO_INCREMENT PRIMARY KEY,
    building_id INT,
    person_id int,
    apartment_id int,
    FOREIGN KEY (building_id) REFERENCES building(id),
    FOREIGN KEY (person_id) REFERENCES person(id),
    FOREIGN KEY (apartment_id) REFERENCES apartment(id)
);
