package praktikum.util;

import praktikum.dto.request.RegisterUserRequest;

import java.util.UUID;

public final class UserGenerator {

    private UserGenerator() {
    }

    public static RegisterUserRequest getRandomUser() {
        String uniquePart = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        return new RegisterUserRequest(
                "test_" + uniquePart + "@yandex.ru",
                "Password123",
                "User_" + uniquePart
        );
    }
}