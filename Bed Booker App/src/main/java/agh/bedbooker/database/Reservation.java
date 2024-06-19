package agh.bedbooker.database;

public class Reservation {
    private int reservationID;
    private String name;
    private String surname;
    private int roomID;
    private int numberOfPlaces;
    private int roomPricePerNight;
    private String startDate;
    private String endDate;
    private int numberOfDays;
    private int price;
    private int discount;
    private String status;

    public Reservation(int reservationID, String name, String surname, int roomID, int numberOfPlaces,
                       int roomPricePerNight, String startDate, String endDate, int numberOfDays, int price, int discount) {
        this.reservationID = reservationID;
        this.name = name;
        this.surname = surname;
        this.roomID = roomID;
        this.numberOfPlaces = numberOfPlaces;
        this.roomPricePerNight = roomPricePerNight;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDays = numberOfDays;
        this.price = price;
        this.discount = discount;
    }

    public int getReservationID() {
        return reservationID;
    }

    public String getName() { return name; }

    public String getSurname() {
        return surname;
    }

    public int getRoomID() {
        return roomID;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public int getRoomPricePerNight() {
        return roomPricePerNight;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }

    public int getNumberOfDays() { return numberOfDays; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
