# MiniProjekt BedBooker

BedBooker to narzędzie ułatwiające zarządzanie hotelem. Jego głównym zastosowaniem jest obsługa bazodanowa rezerwacji. Projekt tworzony jest przy pomocy MySQL oraz Javy.

---

Imiona i nazwiska autorów : Gabriela Dumańska, Katarzyna Lisiecka

---

# Tabele

- `Persons`  - osoby

  - `PersonID` - identyfikator, klucz główny
  - `Name` - imię
  - `Surname` - nazwisko
  - `StreetAddress` - adres
  - `CityID` -  identyfikator miasta, klucz obcy
  - `CountryID` -  identyfikator kraju, klucz obcy
  - `PhoneNumber` -  numer telefonu
  - `Email` - adres e-mail
  ```sql
  CREATE TABLE Persons (
      PersonID int NOT NULL AUTO_INCREMENT,
      Name varchar(20) NOT NULL,
      Surname varchar(20) NOT NULL,
      StreetAddress varchar(50) NOT NULL,
      CityID int NOT NULL,
      CountryID int NOT NULL,
      PhoneNumber varchar(10) NOT NULL,
      Email varchar(50) NOT NULL,
      UNIQUE (Email),
      UNIQUE (PhoneNumber),
      PRIMARY KEY (PersonID),
      CONSTRAINT FK_PersonsCities FOREIGN KEY (CityID) REFERENCES Cities(CityID),
      CONSTRAINT FK_PersonsCountries FOREIGN KEY (CountryID) REFERENCES Countries(CountryID)
  );
  ```

- `Workers` - pracownicy
  - `PersonID` - identyfikator, klucz główny, klucz obcy
  - `Salary` - wypłata
  ```sql
  CREATE TABLE Workers (
      PersonID int NOT NULL,
      Salary int NOT NULL,
      PRIMARY KEY (PersonID),
      CONSTRAINT FK_WorkersPersons FOREIGN KEY (PersonID) REFERENCES Persons(PersonID)
  );
  ```

- `Cities` - słownik miast
  - `CityID` - identyfikator, klucz główny
  - `CityName` - nazwa miasta
  ```sql
  CREATE TABLE Cities (
      CityID int NOT NULL AUTO_INCREMENT,
      CityName varchar(20) NOT NULL,
      PRIMARY KEY (CountryID)
  );
  ```

- `Countries` - słownik państw
  - `CountryID` - identyfikator, klucz główny
  - `CountryName` - nazwa państwa
  ```sql
  CREATE TABLE Countries (
      CountryID int NOT NULL AUTO_INCREMENT,
      CountryName varchar(20) NOT NULL,
      PRIMARY KEY (CountryID)
  );
  ```

- `Rooms` - pokoje
  - `RoomID` - identyfikator, klucz główny
  - `NumberOfPlaces` - liczba miejsc
  - `Price` - cena za pokój za dobę hotelową
  ```sql
  CREATE TABLE Rooms (
      RoomID int NOT NULL AUTO_INCREMENT,
      NumberOfPlace int NOT NULL,
      Price int NOT NULL,
      PRIMARY KEY (RoomID)
  );
  ```

- `Reservations`  - rezerwacje

  - `ReservationID` - identyfikator, klucz główny
  - `PersonID` - identyfikator osoby, klucz obcy
  - `RoomID` - identyfikator pokoju, klucz obcy
  - `StartDate` - początek pobytu
  - `EndDate` -  koniec pobytu
  - `StatusID` -  identyfikator statusu, klucz obcy
  - `Price` -  cena rezerwacji
  - `Discount` - zniżka
  ```sql
  CREATE TABLE Reservations (
      ReservationID int NOT NULL AUTO_INCREMENT,
      PersonID int NOT NULL,
      RoomID int NOT NULL,
      StartDate date NOT NULL,
      EndDate date NOT NULL,
      StatusID int NOT NULL,
      Price int NOT NULL,
      Discount decimal(4,2) NOT NULL,
      PRIMARY KEY (ReservationID),
      CONSTRAINT FK_ReservationsPersons FOREIGN KEY (PersonID) REFERENCES Persons(PersonID),
      CONSTRAINT FK_ReservationsRooms FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID),
      CONSTRAINT FK_ReservationsStatuses FOREIGN KEY (StatusID) REFERENCES Statuses(StatusID),
      CONSTRAINT check_discount_range CHECK ( Discount >= 0.00 AND Discount <= 1.00 )
  );
  ```

- `Damages` - pokoje
  - `DamageID` - identyfikator, klucz główny
  - `ReservationID` - identyfikator rezerwacji, klucz obcy
  - `Date` - data zniszczenia
  - `Price` - wartość szkód
  ```sql
    CREATE TABLE Damages (
      DamageID int NOT NULL AUTO_INCREMENT,
      ReservationID int NOT NULL,
      Date date NOT NULL,
      Price int NOT NULL,
      PRIMARY KEY (DamageID),
      CONSTRAINT FK_DamagesReservations FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID)
  );
  ```

- `Logs` - dziennik zmian statusów rezerwacji

  - `LogID` - identyfikator, klucz główny
  - `ReservationID` - identyfikator rezerwacji, klucz obcy
  - `StatusID` - identyfikator statusu, klucz obcy
  - `Date` - data zmiany
  ```sql
  CREATE TABLE Logs (
      LogID int NOT NULL AUTO_INCREMENT,
      ReservationID int NOT NULL,
      StatusID int NOT NULL,
      DateTime datetime NOT NULL,
      PRIMARY KEY (LogID),
      CONSTRAINT FK_LogsReservations FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID),
      CONSTRAINT FK_LogsStatus FOREIGN KEY (StatusID) REFERENCES Statuses(StatusID)
  );
  ```

- `Statuses` - słownik statusów
  - `StatusID` - identyfikator, klucz główny
  - `StatusName` - nazwa statusu- rezerwacja nowa, potwierdzona i zapłacona, anulowana
  ```sql
  CREATE TABLE Statuses(
      StatusID int NOT NULL AUTO_INCREMENT,
      StatusName ENUM('new', 'confirmed', 'paid', 'cancelled'),
      PRIMARY KEY (StatusID)
  );
  ```

---

# Dane
Dane dodano po utworzeniu triggerów `tr_log_insert` oraz `tr_log_update`, aby tabela `Logs` wypełniała się automatycznie.  

```sql
-- Cities
INSERT INTO Cities (CityName) VALUES 
('Warszawa'),
('Kraków'),
('Gdańsk'),
('Wrocław'),
('Poznań'),
('Lódz'),
('Szczecin'),
('Katowice'),
('Berlin'),
('Hamburg');

--Countries
INSERT INTO Countries (CountryName) VALUES 
('Polska'), 
('Niemcy');

--Persons
INSERT INTO Persons (Name, Surname, StreetAddress, CityID, CountryID, PhoneNumber, Email) VALUES
('Jan', 'Kowalski', 'ul. Kwiatowa 1', 1, 1, '123456781', 'jan.kowalski@example.com'),
('Anna', 'Nowak', 'ul. Słoneczna 2', 2, 1, '123456782', 'anna.nowak@example.com'),
('Andrzej', 'Lewandowski', 'ul. Parkowa 3', 3, 1, '123456783', 'andrzej.lewandowski@example.com'),
('Małgorzata', 'Wójcik', 'ul. Leśna 4', 4, 1, '123456784', 'malgorzata.wojcik@example.com'),
('Piotr', 'Dąbrowski', 'ul. Polna 5', 5, 1, '123456785', 'piotr.dabrowski@example.com'),
('Barbara', 'Kamińska', 'ul. Ogrodowa 6', 6, 1, '123456786', 'barbara.kaminska@example.com'),
('Tomasz', 'Kowalczyk', 'ul. Kwiatowa 7', 7, 1, '123456787', 'tomasz.kowalczyk@example.com'),
('Magdalena', 'Zielińska', 'ul. Słoneczna 8', 8, 1, '123456788', 'magdalena.zielinska@example.com'),
('Paweł', 'Szymański', 'Kaiserstraße 1', 9, 2, '123456789', 'pawel.szymanski@example.com'),
('Krystyna', 'Wojciechowska', 'Bachstraße 2', 10, 2, '123456780', 'krystyna.wojciechowska@example.com'),
('Marek', 'Nowakowski', 'ul. Polna 11', 1, 1, '123456771', 'marek.nowakowski@example.com'),
('Ewa', 'Jankowska', 'ul. Ogrodowa 12', 2, 1, '123456772', 'ewa.jankowska@example.com'),
('Michał', 'Mazur', 'ul. Kwiatowa 13', 3, 1, '123456773', 'michal.mazur@example.com'),
('Jolanta', 'Kowalik', 'ul. Słoneczna 14', 4, 1, '123456774', 'jolanta.kowalik@example.com'),
('Rafał', 'Wilk', 'ul. Parkowa 15', 5, 1, '123456775', 'rafal.wilk@example.com'),
('Agnieszka', 'Wiśniewska', 'ul. Leśna 16', 6, 1, '123456776', 'agnieszka.wisniewska@example.com'),
('Krzysztof', 'Kwiatkowski', 'ul. Polna 17', 7, 1, '123456777', 'krzysztof.kwiatkowski@example.com'),
('Danuta', 'Nowakowska', 'ul. Ogrodowa 18', 8, 1, '123456778', 'danuta.nowakowska@example.com'),
('Artur', 'Kowalczyk', 'Schillerplatz 3', 9, 2, '123456779', 'artur.kowalczyk@example.com'),
('Joanna', 'Zielinska', 'Goethestraße 4', 10, 2, '123456770', 'joanna.zielinska@example.com'),
('Łukasz', 'Lewandowski', 'ul. Parkowa 21', 1, 1, '123456761', 'lukasz.lewandowski@example.com'),
('Katarzyna', 'Wójcik', 'ul. Leśna 22', 2, 1, '123456762', 'katarzyna.wojcik@example.com'),
('Mariusz', 'Szymański', 'ul. Polna 23', 3, 1, '123456763', 'mariusz.szymanski@example.com'),
('Monika', 'Wojciechowska', 'ul. Ogrodowa 24', 4, 1, '123456764', 'monika.wojciechowska@example.com'),
('Wojciech', 'Nowak', 'ul. Kwiatowa 25', 5, 1, '123456765', 'wojciech.nowak@example.com'),
('Alicja', 'Jankowska', 'ul. Słoneczna 26', 6, 1, '123456766', 'alicja.jankowska@example.com'),
('Tadeusz', 'Mazur', 'ul. Parkowa 27', 7, 1, '123456767', 'tadeusz.mazur@example.com'),
('Halina', 'Kowalik', 'ul. Leśna 28', 8, 1, '123456768', 'halina.kowalik@example.com'),
('Dariusz', 'Wilk', 'Lindenstraße 5', 9, 2, '123456769', 'dariusz.wilk@example.com'),
('Iwona', 'Wiśniewska', 'Mühlenweg 6', 10, 2, '123456760', 'iwona.wisniewska@example.com'),
('Marta', 'Kowalczyk', 'ul. Kwiatowa 31', 1, 1, '123456751', 'marta.kowalczyk@example.com'),
('Kamil', 'Zielinski', 'ul. Słoneczna 32', 2, 1, '123456752', 'kamil.zielinski@example.com'),
('Marzena', 'Nowak', 'ul. Parkowa 33', 3, 1, '123456753', 'marzena.nowak@example.com'),
('Tomasz', 'Wójcik', 'ul. Leśna 34', 4, 1, '123456754', 'tomasz.wojcik@example.com'),
('Anna', 'Kowalska', 'ul. Polna 35', 5, 1, '123456755', 'anna.kowalska@example.com'),
('Piotr', 'Jankowski', 'ul. Ogrodowa 36', 6, 1, '123456756', 'piotr.jankowski@example.com'),
('Elżbieta', 'Mazur', 'ul. Kwiatowa 37', 7, 1, '123456757', 'elzbieta.mazur@example.com'),
('Adam', 'Wilk', 'ul. Słoneczna 38', 8, 1, '123456758', 'adam.wilk@example.com'),
('Katarzyna', 'Kowalik', 'Bismarckstraße 7', 9, 2, '123456759', 'katarzyna.kowalik@example.com'),
('Michał', 'Wiśniewski', 'Hauptstraße 8', 10, 2, '123456750', 'michal.wisniewski@example.com'),
('Izabela', 'Nowak', 'ul. Kwiatowa 41', 1, 1, '123456741', 'izabela.nowak@example.com'),
('Krzysztof', 'Kowalski', 'ul. Słoneczna 42', 2, 1, '123456742', 'krzysztof.kowalski@example.com'),
('Agata', 'Wójcik', 'ul. Parkowa 43', 3, 1, '123456743', 'agata.wojcik@example.com'),
('Roman', 'Lewandowski', 'ul. Leśna 44', 4, 1, '123456744', 'roman.lewandowski@example.com'),
('Monika', 'Dąbrowska', 'ul. Polna 45', 5, 1, '123456745', 'monika.dabrowska@example.com'),
('Marek', 'Kamiński', 'ul. Ogrodowa 46', 6, 1, '123456746', 'marek.kaminski@example.com'),
('Anna', 'Kowalczyk', 'ul. Kwiatowa 47', 7, 1, '123456747', 'anna.kowalczyk@example.com'),
('Andrzej', 'Zieliński', 'ul. Słoneczna 48', 8, 1, '123456748', 'andrzej.zielinski@example.com'),
('Małgorzata', 'Szymańska', 'Am Markt 9', 9, 2, '123456749', 'malgorzata.szymanska@example.com'),
('Piotr', 'Wojciechowski', 'Kirchplatz 10', 10, 2, '123456740', 'piotr.wojciechowski@example.com');

--Statuses
INSERT INTO Statuses(StatusName) VALUES
('new'),
('confirmed'),
('paid'),
('cancelled');

--Rooms
INSERT INTO Rooms (NumberOfPlace, Price) VALUES
(1, 100),
(2, 120),
(3, 150),
(4, 200),
(1, 150),
(2, 180),
(3, 200),
(4, 250),
(1, 180),
(2, 190),
(3, 250),
(4, 300),
(1, 50),
(2, 90),
(3, 120),
(4, 190),
(1, 100),
(2, 200),
(3, 300),
(4, 400);
```

Ostatnie dwie tabele, czyli `Reservations` oraz `Logs` nie pojawiają się w rozpisce powyżej. Wynika to z faktu, że dodano dużo rezerwacji, a następno zmieniano ich statusy. Więc obecny stan bazy danych i tak nie będzie z nimi zgodny. Dodatkowo tabela `Logs` wypełniała się automatycznie dzięki utworzonym triggerom (ich opis i dowód działania znajduje się w sekcji poświęconej triggerom). 
---

# Widoki 

---

# Procedury, funkcje
funkcja na dodawanie rezerwacji powinna sama ustawiać cenę za pokój
---

# Triggery

```sql
CREATE TRIGGER tr_log_insert AFTER INSERT ON Reservations
    FOR EACH ROW
    BEGIN
        INSERT INTO Logs (ReservationID, StatusID, DateTime) VALUES (NEW.ReservationID, NEW.StatusID, NOW());
    END;

CREATE TRIGGER tr_log_update AFTER UPDATE ON Reservations
    FOR EACH ROW
    BEGIN
        INSERT INTO Logs (ReservationID, StatusID, DateTime) VALUES (NEW.ReservationID, NEW.StatusID, NOW());
    END;
```

---
