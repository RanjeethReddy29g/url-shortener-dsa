import java.util.HashMap;
import java.util.Random;

public class ShortURLService {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;

    private HashMap<String, String> shortToLong = new HashMap<>();
    private HashMap<String, String> longToShort = new HashMap<>();
    private Random random = new Random();

    // Generate short URL
    public String shortenURL(String longURL) {

        // If already shortened
        if (longToShort.containsKey(longURL)) {
            return longToShort.get(longURL);
        }

        String shortCode;
        do {
            shortCode = generateCode();
        } while (shortToLong.containsKey(shortCode)); // collision handling

        shortToLong.put(shortCode, longURL);
        longToShort.put(longURL, shortCode);

        return shortCode;
    }

    // Retrieve original URL
   public String getOriginalURL(String shortInput) {

    // Extract shortcode if full URL is entered
    if (shortInput.contains("/")) {
        shortInput = shortInput.substring(shortInput.lastIndexOf("/") + 1);
    }

    return shortToLong.getOrDefault(shortInput, "URL not found");
}

    // Generate random Base62 code
    private String generateCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }
        return sb.toString();
    }
}
