package praktikum;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserGenerator {

    public static Map<String, String> getRandomUser() {
        String uniquePart = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        Map<String, String> user = new HashMap<>();
        user.put("email", "test_" + uniquePart + "@yandex.ru");
        user.put("password", "Password123");
        user.put("name", "User_" + uniquePart);

        return user;
    }
}