CREATE DATABASE emensawerbeseite
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
DROP DATABASE emensawerbeseite;

USE emensawerbeseite;
CREATE TABLE gericht (
                         id int8 PRIMARY KEY ,
                         name varchar(80) not null unique ,
                         beschreibung varchar(800) not null ,
                         erfasst_am date not null,
                         vegetarisch BOOLEAN NOT NULL DEFAULT FALSE,
                         vegan BOOLEAN NOT NULL DEFAULT FALSE,
                         preisintern DOUBLE NOT NULL,
                         preisextern DOUBLE NOT NULL,
                         CHECK (preisintern <= preisextern AND preisintern > 0)
);

CREATE TABLE  allergen(
                          code char(4) PRIMARY KEY ,
                          name varchar(300) not null ,
                          typ varchar(20) not null DEFAULT 'allergen'
);

CREATE TABLE kategorie (
                           id int8 PRIMARY KEY,
                           name VARCHAR(80) NOT NULL,
                           eltern_id int8 REFERENCES kategorie (id) ,
                           bildname VARCHAR(200)
);

CREATE TABLE gericht_hat_allergen (
                                      code CHAR(4) NOT NULL REFERENCES allergen(code),
                                      gericht_id int8 NOT NULL REFERENCES gericht(id)
);

CREATE TABLE gericht_hat_kategorie (
                                       gericht_id int8 NOT NULL REFERENCES gericht(id),
                                       kategorie_id int8 NOT NULL REFERENCES kategorie(id)
);

INSERT INTO `allergen` (`code`, `name`, `typ`) VALUES
	('a', 'Getreideprodukte', 'Getreide (Gluten)'),
	('a1', 'Weizen', 'Allergen'),
	('a2', 'Roggen', 'Allergen'),
	('a3', 'Gerste', 'Allergen'),
	('a4', 'Dinkel', 'Allergen'),
	('a5', 'Hafer', 'Allergen'),
	('a6', 'Dinkel', 'Allergen'),
	('b', 'Fisch', 'Allergen'),
	('c', 'Krebstiere', 'Allergen'),
	('d', 'Schwefeldioxid/Sulfit', 'Allergen'),
	('e', 'Sellerie', 'Allergen'),
	('f', 'Milch und Laktose', 'Allergen'),
	('f1', 'Butter', 'Allergen'),
	('f2', 'Käse', 'Allergen'),
	('f3', 'Margarine', 'Allergen'),
	('g', 'Sesam', 'Allergen'),
	('h', 'Nüsse', 'Allergen'),
	('h1', 'Mandeln', 'Allergen'),
	('h2', 'Haselnüsse', 'Allergen'),
	('h3', 'Walnüsse', 'Allergen'),
	('i', 'Erdnüsse', 'Allergen');

INSERT INTO `gericht` (`id`, `name`, `beschreibung`, `erfasst_am`, `vegan`, `vegetarisch`, `preisintern`, `preisextern`) VALUES
	(1, 'Bratkartoffeln mit Speck und Zwiebeln', 'Kartoffeln mit Zwiebeln und gut Speck', '2020-08-25', 0, 0, 2.3, 4),
	(3, 'Bratkartoffeln mit Zwiebeln', 'Kartoffeln mit Zwiebeln und ohne Speck', '2020-08-25', 1, 1, 2.3, 4),
	(4, 'Grilltofu', 'Fein gewürzt und mariniert', '2020-08-25', 1, 1, 2.5, 4.5),
	(5, 'Lasagne', 'Klassisch mit Bolognesesoße und Creme Fraiche', '2020-08-24', 0, 0, 2.5, 4.5),
	(6, 'Lasagne vegetarisch', 'Klassisch mit Sojagranulatsoße und Creme Fraiche', '2020-08-24', 0, 1, 2.5, 4.5),
	(7, 'Hackbraten', 'Nicht nur für Hacker', '2020-08-25', 0, 0, 2.5, 4),
	(8, 'Gemüsepfanne', 'Gesundes aus der Region, deftig angebraten', '2020-08-25', 1, 1, 2.3, 4),
	(9, 'Hühnersuppe', 'Suppenhuhn trifft Petersilie', '2020-08-25', 0, 0, 2, 3.5),
	(10, 'Forellenfilet', 'mit Kartoffeln und Dilldip', '2020-08-22', 0, 0, 3.8, 5),
	(11, 'Kartoffel-Lauch-Suppe', 'der klassische Bauchwärmer mit frischen Kräutern', '2020-08-22', 0, 1, 2, 3),
	(12, 'Kassler mit Rosmarinkartoffeln', 'dazu Salat und Senf', '2020-08-23', 0, 0, 3.8, 5.2),
	(13, 'Drei Reibekuchen mit Apfelmus', 'grob geriebene Kartoffeln aus der Region', '2020-08-23', 0, 1, 2.5, 4.5),
	(14, 'Pilzpfanne', 'die legendäre Pfanne aus Pilzen der Saison', '2020-08-23', 0, 1, 3, 5),
	(15, 'Pilzpfanne vegan', 'die legendäre Pfanne aus Pilzen der Saison ohne Käse', '2020-08-24', 1, 1, 3, 5),
	(16, 'Käsebrötchen', 'schmeckt vor und nach dem Essen', '2020-08-24', 0, 1, 1, 1.5),
	(17, 'Schinkenbrötchen', 'schmeckt auch ohne Hunger', '2020-08-25', 0, 0, 1.25, 1.75),
	(18, 'Tomatenbrötchen', 'mit Schnittlauch und Zwiebeln', '2020-08-25', 1, 1, 1, 1.5),
	(19, 'Mousse au Chocolat', 'sahnige schweizer Schokolade rundet jedes Essen ab', '2020-08-26', 0, 1, 1.25, 1.75),
	(20, 'Suppenkreation á la Chef', 'was verschafft werden muss, gut und günstig', '2020-08-26', 0, 0, 0.5, 0.9);

INSERT INTO `gericht_hat_allergen` (`code`, `gericht_id`) VALUES
	('h', 1),	
	('a3', 1),	
	('a4', 1),	
	('f1', 3),	
	('a6', 3),	
	('i', 3),	
	('a3', 4),	
	('f1', 4),	
	('a4', 4),	
	('h3', 4),	
	('d', 6),	
	('h1',7),	
	('a2', 7),	
	('h3', 7),	
	('c', 7),	
	('a3', 8),	
	('h3', 10),	
	('d', 10),	
	('f', 10),	
	('f2', 12),	
	('h1', 12),	
	('a5',12),	
	('c', 1),	
	('a2', 9),	
	('i', 14),	
	('f1', 1),	
	('a1', 15),	
	('a4', 15),	
	('i', 15),	
	('f3', 15),	
	('h3', 15);

INSERT INTO `kategorie` (`id`, `eltern_id`, `name`, `bildname`) VALUES
	(1, NULL, 'Aktionen', 'kat_aktionen.png'),
	(2, NULL, 'Menus', 'kat_menu.gif'),
	(3, 2, 'Hauptspeisen', 'kat_menu_haupt.bmp'),
	(4, 2, 'Vorspeisen', 'kat_menu_vor.svg'),
	(5, 2, 'Desserts', 'kat_menu_dessert.pic'),
	(6, 1, 'Mensastars', 'kat_stars.tif'),
	(7, 1, 'Erstiewoche', 'kat_erties.jpg');

INSERT INTO `gericht_hat_kategorie` (`kategorie_id`, `gericht_id`) VALUES
	(3, 1),	(3, 3),	(3, 4),	(3, 5),	(3, 6),	(3, 7),	(3, 9),	(4, 16), (4, 17), (4, 18), (5, 16), (5, 17), (5, 18);

-- Aufgabe 2
SELECT count(*) FROM kategorie as anz_kategorie;
SELECT count(*) FROM allergen as anz_allergen;
SELECT count(*) FROM gericht AS anz_gericht;
SELECT count(*) FROM gericht_hat_allergen AS anz_gericht_hat_allergen;
SELECT count(*) FROM gericht_hat_kategorie AS anz_gericht_hat_kategorie;

SELECT * FROM gericht_hat_kategorie ;
/**/


-- Aufgabe 3
/*1)  alle Gerichte*/
SELECT * FROM gericht;

/*2)  */
SELECT name, erfasst_am  FROM gericht;

/*3) sortiert absteigen  */
SELECT   name AS Gerichtname ,erfasst_am FROM gericht
ORDER BY Gerichtname DESC ;

/*4) sortiert aufsteigen nur 5 */
SELECT  name, beschreibung FROM gericht
ORDER BY name ASC LIMIT 5;

/*5) sortiert aufsteigen nur 5 beginend */
SELECT  name, beschreibung FROM gericht
ORDER BY name ASC LIMIT 5 OFFSET 5 ;

/*6) Alle möglichen Allergen-Typen (ohne Duplikate).*/
SELECT DISTINCT typ FROM allergen;


/*7) Alle Gericht deren Name mit L / l beginnt*/
SELECT name FROM gericht WHERE name LIKE 'L%';

/*8) Alle Gericht deren Name= suppe an beliebiger Stelle absteigend */
SELECT id, name FROM gericht WHERE name LIKE '%suppe%';

/*9) Alle Kategorien, die keinen Elterneintrag besitzen */
SELECT * FROM kategorie WHERE eltern_id is NULL;

/*10) Dinkel zu Kamut */
UPDATE allergen set name='Kamut' where code='a6';
SELECT * FROM allergen;

/*11) Gericht „Currywurst mit Pommes“ hinzufügen und in Kategorie „Hauptspeisen“ eintragen*/
INSERT INTO gericht (id, name, beschreibung, erfasst_am, vegan, vegetarisch, preisintern, preisextern)
VALUES (21, 'Currywurst mit Pommes',
        'Currywurst mit knusprigen Pommes',
        '2020-08-27', 0, 0, 2.8, 4.5);
/* Gericht der Kategorie „Hauptspeisen“ (id = 3)*/
INSERT INTO gericht_hat_kategorie (kategorie_id, gericht_id)
VALUES (3, 21);
SELECT * FROM gericht ;

-- Aufgabe 6-1

SELECT
    g.id           AS gericht_id,
    g.name         AS gericht_name,
    a.code         AS allergen_code,
    a.name         AS allergen_name
FROM gericht g
         JOIN gericht_hat_allergen gha
              ON g.id = gha.gericht_id
         JOIN allergen a
              ON gha.code = a.code
ORDER BY
    g.name,
    a.code;
-- Aufgabe 6-2
SELECT g.id   AS gericht_id,
       g.name AS gericht_name,
       a.code AS allergen_code,
       a.name AS allergen_name
FROM gericht g
         LEFT JOIN gericht_hat_allergen gha
                   ON g.id = gha.gericht_id
         LEFT JOIN allergen a
                   ON gha.code = a.code
ORDER BY
    g.name,
    a.code;

-- Aufgabe 6-3
SELECT
    a.code        AS allergen_code,
    a.name        AS allergen_name,
    g.id          AS gericht_id,
    g.name        AS gericht_name
FROM allergen a
         LEFT JOIN gericht_hat_allergen gha
                   ON a.code = gha.code
         LEFT JOIN gericht g
                   ON gha.gericht_id = g.id
ORDER BY
    a.code,
    g.name;

-- Aufgabe 6-4
SELECT
    k.id   AS kategorie_id,
    k.name AS kategorie_name,
    COUNT(g.id) AS anzahl_gerichte
FROM kategorie k
         JOIN gericht_hat_kategorie ghk
              ON k.id = ghk.kategorie_id
         JOIN gericht g
              ON ghk.gericht_id = g.id
GROUP BY
    k.id,
    k.name
ORDER BY
    anzahl_gerichte ASC,
    k.name;

-- Aufgabe 6-5
SELECT
    k.id   AS kategorie_id,
    k.name AS kategorie_name,
    COUNT(g.id) AS anzahl_gerichte
FROM kategorie k
         JOIN gericht_hat_kategorie ghk
              ON k.id = ghk.kategorie_id
         JOIN gericht g
              ON ghk.gericht_id = g.id
GROUP BY
    k.id,
    k.name
HAVING
    COUNT(g.id) > 2
ORDER BY
    anzahl_gerichte ASC,
    k.name;


-- Aufgabe 7
/*✔️ 1. Tabelle gericht_hat_allergen*/
ALTER TABLE gericht_hat_allergen
    ADD CONSTRAINT fk_gha_gericht
        FOREIGN KEY (gericht_id) REFERENCES gericht(id)
            ON DELETE CASCADE;

ALTER TABLE gericht_hat_allergen
    ADD CONSTRAINT fk_gha_allergen
        FOREIGN KEY (code) REFERENCES allergen(code)
            ON DELETE CASCADE;
/*✔️ 2. Tabelle gericht_hat_kategorie*/
ALTER TABLE gericht_hat_kategorie
    ADD CONSTRAINT fk_ghk_gericht
        FOREIGN KEY (gericht_id) REFERENCES gericht(id)
            ON DELETE CASCADE;

ALTER TABLE gericht_hat_kategorie
    ADD CONSTRAINT fk_ghk_kategorie
        FOREIGN KEY (kategorie_id) REFERENCES kategorie(id)
            ON DELETE CASCADE;

/*✔️ 3. Tabelle allergen*/

ALTER TABLE allergen
    ADD CONSTRAINT unq_allergen_code UNIQUE (code);

/*✔️ 4. Tabelle gericht*/

ALTER TABLE gericht
    ADD CONSTRAINT unq_gericht_name UNIQUE (name);

/*✔️ 5. Tabelle kategorie*/
ALTER TABLE kategorie
    ADD CONSTRAINT unq_kategorie_name UNIQUE (name);
-- Aufgabe 9
CREATE TABLE IF NOT EXISTS besuch (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      visited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


SELECT DISTINCT a.code, a.name
FROM allergen a
         JOIN gericht_hat_allergen gha ON a.code = gha.code

ORDER BY a.code;