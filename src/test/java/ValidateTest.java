import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.HttpMethod;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateTest {
    @Test
    void validateUserTestName(){
        User user = new User(12, "email@", "login", "name", LocalDate.now());

        UserController controller = new UserController(userService);
        final boolean validateTest = controller.validate(user, HttpMethod.POST);
        final String nameTest = user.getName();

        assertNotNull(nameTest, "name isNull");
        assertEquals(nameTest, "name");
        assertEquals(validateTest, true);
    }

    @Test
    void validateUserTestNameIsEmpty(){
        User user = new User(12, "email@", "login", null, LocalDate.now());

        UserController controller = new UserController(userService);
        final boolean validateTest = controller.validate(user, HttpMethod.POST);
        final String nameTest = user.getName();

        assertNotNull(nameTest, "name isNull");
        assertEquals(nameTest, "login");
        assertEquals(validateTest, true);
    }

    @Test
    void validateUserTestLogin(){
        User user = new User(12, "email@", "login", "name", LocalDate.now());

        UserController controller = new UserController(userService);
        final boolean validateTest = controller.validate(user, HttpMethod.POST);
        final String loginTest = user.getLogin();

        assertNotNull(loginTest, "login isNull");
        assertEquals(loginTest, "login");
        assertEquals(validateTest, true);
    }

    @Test
    void validateUserTestLoginIsEmpty(){
        User user = new User(12, "email@", "", "name", LocalDate.now());
        UserController controller = new UserController(userService);

        try {
            controller.validate(user, HttpMethod.POST);
        }
        catch (ResponseStatusException exception) {
            assertEquals("400 BAD_REQUEST", exception.getMessage());
        }

        final String loginTest = user.getLogin();

        assertNotNull(loginTest, "login isNull");
        assertEquals(loginTest, "");
    }

    @Test
    void validateFilmTestRelease(){
        Film film = new Film(12, "film", "description", LocalDate.now().minusYears(10), 120);

        FilmController controller = new FilmController();
        final boolean validateTest = controller.validate(film, HttpMethod.POST);
        final LocalDate releaseTest = film.getReleaseDate();

        assertNotNull(releaseTest, "release isNull");
        assertEquals(releaseTest, LocalDate.now().minusYears(10));
        assertEquals(validateTest, true);
    }

    @Test
    void validateFilmTestReleaseIsBefore(){
        Film film = new Film(12, "film", "description", LocalDate.now().minusYears(200), 120);
        FilmController controller = new FilmController();

        try {
            controller.validate(film, HttpMethod.POST);
        }
        catch (ResponseStatusException exception) {
            assertEquals("400 BAD_REQUEST", exception.getMessage());
        }

        final LocalDate releaseTest = film.getReleaseDate();

        assertNotNull(releaseTest, "release isNull");
        assertEquals(releaseTest, LocalDate.now().minusYears(200));
    }
}

