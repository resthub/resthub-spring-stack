CREATE TABLE table2 (
	id integer NOT NULL,
	PRIMARY KEY  (id)
);
CREATE TABLE table1 (
	id integer NOT NULL,
	table2_id integer,
	PRIMARY KEY  (id),
	CONSTRAINT table2FK FOREIGN KEY (table2_id) REFERENCES table2 (id)
);