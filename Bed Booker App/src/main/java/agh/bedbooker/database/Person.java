package agh.bedbooker.database;

public class Person {
    private int personID;
    private String name;
    private String surname;
    private String streetAddress;
    private String city;
    private String country;
    private String email;
    private String phoneNumber;
    private boolean isBanned;
    private boolean isRegular;

    public Person(int personID, String name, String surname, String streetAddress, String city, String country,
                  String email, String phoneNumber, boolean isBanned, boolean isRegular) {
        this.personID = personID;
        this.name = name;
        this.surname = surname;
        this.streetAddress = streetAddress;
        this.city = city;
        this.country = country;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isBanned = isBanned;
        this.isRegular = isRegular;
    }
    public String getIsBanned() {
        if(isBanned) {
            return "Tak";
        }
        return "Nie";
    }

    public String getIsRegular() {
        if(isRegular) {
            return "Tak";
        }
        return "Nie";
    }
    public boolean isBanned() {
        return isBanned;
    }

    public boolean isRegular() {
        return isRegular;
    }

    public int getPersonID() {
        return personID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


}
