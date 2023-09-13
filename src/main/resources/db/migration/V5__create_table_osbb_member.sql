CREATE TABLE osbb_member (
    id INT AUTO_INCREMENT PRIMARY KEY,
    person_id int,
    osbb_role_id int,
    FOREIGN KEY (person_id) REFERENCES person(id),
    FOREIGN KEY (osbb_role_id) REFERENCES osbb_role(id)
);
