import java.util.Scanner;

public class URLShortener {

    public static void main(String[] args) {

        ShortURLService service = new ShortURLService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Shorten URL");
            System.out.println("2. Get Original URL");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            String input = sc.nextLine(); // take input as STRING
            int choice;

            try {
                choice = Integer.parseInt(input); // convert safely
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid choice. Please enter 1, 2, or 3.");
                continue;
            }

            if (choice == 1) {
                System.out.print("Enter Long URL: ");
                String longURL = sc.nextLine();
                String shortCode = service.shortenURL(longURL);
                System.out.println("Short URL: http://short.ly/" + shortCode);

            } else if (choice == 2) {
                System.out.print("Enter Short URL or Code: ");
                String shortInput = sc.nextLine();
                System.out.println("Original URL: " + service.getOriginalURL(shortInput));

            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;

            } else {
                System.out.println("❌ Invalid choice. Please enter 1, 2, or 3.");
            }
        }

        sc.close();
    }
}
