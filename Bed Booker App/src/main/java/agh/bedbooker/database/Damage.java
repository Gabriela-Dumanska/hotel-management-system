package agh.bedbooker.database;

public class Damage {
    private int damageID;
    private int reservationID;
    private String date;
    private int price;
    public Damage(int damageID, int reservationID, String date, int price) {
        this.damageID = damageID;
        this.reservationID = reservationID;
        this.date = date;
        this.price = price;
    }

    public int getDamageID() {
        return damageID;
    }

    public int getReservationID() {
        return reservationID;
    }

    public int getPrice() {
        return price;
    }

    public String getDate() { return date; }
}