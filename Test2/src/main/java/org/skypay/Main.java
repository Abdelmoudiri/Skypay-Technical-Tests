
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Service service = new Service();

        System.out.println("=== INITIALISATION ===");
        

        service.setRoom(1, RoomType.STANDARD, 1000);
        service.setRoom(2, RoomType.JUNIOR, 2000);
        service.setRoom(3, RoomType.SUITE, 3000);


        service.setUser(1, 5000);
        service.setUser(2, 10000);

        System.out.println("\n=== SCENARIOS ===");
        
        Date d30_06 = makeDate(2026, 6, 30);
        Date d07_07 = makeDate(2026, 7, 7);
        Date d08_07 = makeDate(2026, 7, 8);
        Date d09_07 = makeDate(2026, 7, 9);

        System.out.print("1. User 1 book Room 2 (cher): ");
        service.bookRoom(1, 2, d30_06, d07_07);

        System.out.print("2. User 1 book Room 2 (dates invalides): ");
        service.bookRoom(1, 2, d07_07, d30_06);

        System.out.print("3. User 1 book Room 1 (ok): ");
        service.bookRoom(1, 1, d07_07, d08_07);

        System.out.print("4. User 2 book Room 1 (overlap): ");
        service.bookRoom(2, 1, d07_07, d09_07);

        System.out.print("5. User 2 book Room 3 (ok): ");
        service.bookRoom(2, 3, d07_07, d08_07);

        System.out.println("\n=== MISE A JOUR CHAMBRE ===");
        service.setRoom(1, RoomType.SUITE, 10000);
        System.out.println("Room 1 mise à jour (Type SUITE, Prix 10000)");

        System.out.println("\n=== RESULTATS FINAUX ===");
        service.printAll(); 
        System.out.println();
        service.printAllUsers();
    }

    private static Date makeDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day); 
        return cal.getTime();
    }
}