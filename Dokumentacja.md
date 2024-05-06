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
      Email varchar(30) NOT NULL,
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

- `Damages` - pokoje
  - `DamageID` - identyfikator, klucz główny
  - `ReservationID` - identyfikator rezerwacji, klucz obcy
  - `Date` - data zniszczenia
  - `Price` - wartość szkód

- `Logs` - dziennik zmian statusów rezerwacji

  - `LogID` - identyfikator, klucz główny
  - `ReservationID` - identyfikator rezerwacji, klucz obcy
  - `StatusID` - identyfikator statusu, klucz obcy
  - `Date` - data zmiany

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

# Zadanie 0 - modyfikacja danych, transakcje

---

# Zadanie 1 - widoki


---
# Zadanie 1  - rozwiązanie



```sql

```

---

# Zadanie 2  - funkcje


# Zadanie 2  - rozwiązanie


```sql

```
