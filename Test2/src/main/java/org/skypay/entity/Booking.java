package org.skypay.entity;

import org.skypay.entity.enums.RoomType;
import java.time.LocalDate;

public class Booking {
    private User user;
    private int roomNumber;
    
    private int snapshotPrice; 
    private RoomType snapshotType;
    
    private LocalDate checkIn;
    private LocalDate checkOut;

    public Booking(User user, Room room, LocalDate checkIn, LocalDate checkOut) {
        this.user = user;
        this.roomNumber = room.getRoomNumber();
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.snapshotPrice = room.getPricePerNight();
        this.snapshotType = room.getType();
    }

    public int getRoomNumber() { return roomNumber; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }

    @Override
    public String toString() {
        return "Booking: User " + user.getId() + " -> Room " + roomNumber + 
               " (" + snapshotType + " at " + snapshotPrice + ") from " + checkIn + " to " + checkOut;
    }
}