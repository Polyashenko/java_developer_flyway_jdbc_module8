CREATE TABLE resident (
    id INT AUTO_INCREMENT PRIMARY KEY,
    person_id INT,
    apartment_id INT,
    entry_right boolean,
    FOREIGN KEY (person_id) REFERENCES person(id),
    FOREIGN KEY (apartment_id) REFERENCES apartment(id)
);
