package agh.bedbooker.database;

public class Room {
    private int roomId;
    private int numberOfPlaces;
    private int pricePerNight;

    public Room(int roomId, int numberOfPlaces, int pricePerNight) {
        this.roomId = roomId;
        this.numberOfPlaces = numberOfPlaces;
        this.pricePerNight = pricePerNight;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }
}