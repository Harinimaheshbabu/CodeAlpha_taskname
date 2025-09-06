import java.io.*;
import java.util.*;

class Room {
    int number;
    boolean booked;

    Room(int number) {
        this.number = number;
        this.booked = false;
    }
}

public class SimpleHotel {
    static ArrayList<Room> rooms = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // Create some rooms
        for (int i = 101; i <= 105; i++) {
            rooms.add(new Room(i));
        }

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n---Hotel Room Bookings---");
            System.out.println("\n1.Show Rooms  \n2.Book Room  \n3.Cancel Booking  \n4.Exit");
            int ch = sc.nextInt();

            if (ch == 1) showRooms();
            else if (ch == 2) {
                System.out.print("Enter room number: ");
                bookRoom(sc.nextInt());
            } else if (ch == 3) {
                System.out.print("Enter room number: ");
                cancelRoom(sc.nextInt());
            } else if (ch == 4) break;
            else System.out.println("Invalid!");
        }
    }

    static void showRooms() {
        for (Room r : rooms) {
            System.out.println(r.number + " - " + (r.booked ? "Booked" : "Available"));
        }
    }

    static void bookRoom(int num) throws IOException {
        for (Room r : rooms) {
            if (r.number == num && !r.booked) {
                r.booked = true;
                saveData();
                System.out.println("Room booked!");
                return;
            }
        }
        System.out.println("Room not available!");
    }

    static void cancelRoom(int num) throws IOException {
        for (Room r : rooms) {
            if (r.number == num && r.booked) {
                r.booked = false;
                saveData();
                System.out.println("Booking cancelled!");
                return;
            }
        }
        System.out.println("Room not found or not booked!");
    }

    static void saveData() throws IOException {
        try (FileWriter fw = new FileWriter("bookings.txt")) {
            for (Room r : rooms) {
                fw.write(r.number + "," + r.booked + "\n");
            }
        }
    }
}