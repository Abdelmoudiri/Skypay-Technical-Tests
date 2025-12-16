package org.skypay.service;

import org.skypay.entity.Room;
import org.skypay.entity.User;
import org.skypay.entity.Booking;
import org.skypay.entity.enums.RoomType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class Service {
    private ArrayList<Room> rooms = new ArrayList<>(); 
    private ArrayList<User> users = new ArrayList<>(); 
    private ArrayList<Booking> bookings = new ArrayList<>(); 

    public void setRoom(int roomNumber, RoomType roomType, int roomPricePerNight) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNumber) {

                r.setType(roomType);
                r.setPricePerNight(roomPricePerNight);
                return;
            }
        }
        rooms.add(new Room(roomNumber, roomType, roomPricePerNight));
    }

    public void setUser(int userId, int balance) {
        for (User u : users) {
            if (u.getId() == userId) {
                u.setBalance(balance);
                return;
            }
        }
        users.add(new User(userId, balance));
    }


    public void bookRoom(int userId, int roomNumber, Date checkinDate, Date checkOutDate) {
        try {
            LocalDate checkIn = convertToLocalDate(checkinDate);
            LocalDate checkOut = convertToLocalDate(checkOutDate);

            if (!checkOut.isAfter(checkIn)) {
                System.out.println("Erreur: La date de fin doit être après la date de début.");
                return;
            }

            User user = findUser(userId);
            Room room = findRoom(roomNumber);
            
            if (user == null || room == null) {
                System.out.println("Erreur: Utilisateur ou Chambre introuvable.");
                return;
            }

            if (!isRoomFree(roomNumber, checkIn, checkOut)) {
                System.out.println("Erreur: La chambre " + roomNumber + " n'est pas disponible sur cette période.");
                return;
            }

            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            int totalPrice = (int) (nights * room.getPricePerNight());

            if (user.getBalance() < totalPrice) {
                System.out.println("Erreur: Solde insuffisant pour l'utilisateur " + userId + ". (Requis: " + totalPrice + ", Dispo: " + user.getBalance() + ")");
                return;
            }

            user.setBalance(user.getBalance() - totalPrice);
            Booking newBooking = new Booking(user, room, checkIn, checkOut);
            bookings.add(newBooking);
            
            System.out.println("Succès: Chambre " + roomNumber + " réservée par User " + userId + " pour " + nights + " nuits.");

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public void printAllUsers() {
        System.out.println("--- LISTE DES USERS (Récent -> Ancien) ---");
        ArrayList<User> reversedUsers = new ArrayList<>(users);
        Collections.reverse(reversedUsers); 
        for (User u : reversedUsers) {
            System.out.println(u);
        }
    }

    public void printAll() {
        System.out.println("--- LISTE DES CHAMBRES (Récent -> Ancien) ---");
        ArrayList<Room> reversedRooms = new ArrayList<>(rooms);
        Collections.reverse(reversedRooms);
        for (Room r : reversedRooms) {
            System.out.println(r);
        }

        System.out.println("--- HISTORIQUE RESERVATIONS (Récent -> Ancien) ---");
        ArrayList<Booking> reversedBookings = new ArrayList<>(bookings);
        Collections.reverse(reversedBookings);
        for (Booking b : reversedBookings) {
            System.out.println(b);
        }
    }

    private User findUser(int id) {
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    private Room findRoom(int number) {
        return rooms.stream().filter(r -> r.getRoomNumber() == number).findFirst().orElse(null);
    }

    private boolean isRoomFree(int roomNumber, LocalDate newStart, LocalDate newEnd) {
        for (Booking b : bookings) {
            if (b.getRoomNumber() == roomNumber) {

                if (newStart.isBefore(b.getCheckOut()) && newEnd.isAfter(b.getCheckIn())) {
                    return false;
                }
            }
        }
        return true;
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}