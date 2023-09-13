SELECT
    p.name AS "ПІБ власника",
    p.email AS "Електронна пошта власника",
    b.address AS "Адреса будинку",
    a.number AS "Номер квартири",
    a.area AS "Площа квартири"
FROM
    person AS p
JOIN
    aparnment_ownership AS o ON p.id = o.person_id
JOIN
    apartment AS a ON o.apartment_id = a.id
JOIN
    building AS b ON a.building_id = b.id
JOIN
    resident AS r ON p.id = r.person_id
WHERE
	r.entry_right = 1
GROUP BY
    p.id, b.address, a.number, a.area
HAVING
    COUNT(a.id) < 2;
