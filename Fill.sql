DELETE FROM Plugins
INSERT INTO Plugins VALUES('Main Menu')
INSERT INTO Plugins VALUES('People')
INSERT INTO Plugins VALUES('Addresses')
INSERT INTO Plugins VALUES('Countries')
INSERT INTO Plugins VALUES('Contact types')
INSERT INTO Plugins VALUES('Contact information')
SELECT * FROM Plugins

DELETE FROM RefBoxViews
INSERT INTO RefBoxViews VALUES('Login', 'V_Login')
INSERT INTO RefBoxViews VALUES('Plugins', 'V_Plugins')
INSERT INTO RefBoxViews VALUES('Languages', 'V_Languages')
INSERT INTO RefBoxViews VALUES('People', 'V_People')
INSERT INTO RefBoxViews VALUES('Addresses', 'V_Addresses')
INSERT INTO RefBoxViews VALUES('Countries', 'V_Countries')
INSERT INTO RefBoxViews VALUES('ContactTypes', 'V_ContactTypes')
INSERT INTO RefBoxViews VALUES('ContactInformation', 'V_ContactInformation')
SELECT * FROM RefBoxViews

INSERT INTO Countries VALUES ('DE', 'DEU', 'Deutschland', '+49')
SELECT * FROM Countries

INSERT INTO Addresses VALUES (1, '48167', 'Münster', 'Agathastraße', '90')
INSERT INTO Addresses VALUES (1, '48159', 'Münster', 'Diesterwegstraße', '17')
INSERT INTO Addresses VALUES (1, '48155', 'Münster', 'Liboristraße', NULL)
SELECT * FROM Addresses

INSERT INTO People VALUES ('DavTo', 'Toboll', 'David', '13579', 1, 3)
INSERT INTO People VALUES ('InBa', 'Bahloul', 'Ines', '67890', 1, 2)
INSERT INTO People VALUES ('LuWec', 'Weckermann', 'Lucas', '12345', 1, 1)
INSERT INTO People VALUES ('Chriss', 'Weckermann', 'Christian', '24680', 1, 1)
SELECT * FROM People

INSERT INTO ContactTypes VALUES ('Telefon', 'Tel', NULL);
INSERT INTO ContactTypes VALUES ('Fax', 'Fax', NULL);
INSERT INTO ContactTypes VALUES ('E-Mail', 'Mail', NULL);
SELECT * FROM ContactTypes

DELETE FROM Languages
INSERT INTO Languages VALUES ('Deutsch', 'DEU')
INSERT INTO Languages VALUES ('English', 'USA')
SELECT * FROM Languages