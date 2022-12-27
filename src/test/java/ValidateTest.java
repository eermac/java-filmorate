import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateTest {
    @Test
    void validateUserTestName(){
        User user = new User(12, "email@", "login", "name", LocalDate.now());

        UserController controller = new UserController();
        final boolean validateTest = controller.validate(user);
        final String nameTest = user.getName();

        assertNotNull(nameTest, "name isNull");
        assertEquals(nameTest, "name");
        assertEquals(validateTest, true);
    }

    @Test
    void validateUserTestNameIsEmpty(){
        User user = new User(12, "email@", "login", "", LocalDate.now());

        UserController controller = new UserController();
        final boolean validateTest = controller.validate(user);
        final String nameTest = user.getName();

        assertNotNull(nameTest, "name isNull");
        assertEquals(nameTest, "login");
        assertEquals(validateTest, true);
    }

    @Test
    void validateUserTestEmail(){
        User user = new User(12, "email@", "login", "name", LocalDate.now());

        UserController controller = new UserController();
        final boolean validateTest = controller.validate(user);
        final String emailTest = user.getEmail();

        assertNotNull(emailTest, "email isNull");
        assertEquals(emailTest, "email@");
        assertEquals(validateTest, true);
    }

    @Test
    void validateUserTestEmailIsEmpty(){
        User user = new User(12, "", "login", "name", LocalDate.now());
        UserController controller = new UserController();

        try {
            controller.validate(user);
        }
        catch (ResponseStatusException exception) {
            assertEquals("400 BAD_REQUEST", exception.getMessage());
        }

        final String emailTest = user.getEmail();

        assertNotNull(emailTest, "email isNull");
        assertEquals(emailTest, "");
    }

    @Test
    void validateUserTestLogin(){
        User user = new User(12, "email@", "login", "name", LocalDate.now());

        UserController controller = new UserController();
        final boolean validateTest = controller.validate(user);
        final String loginTest = user.getLogin();

        assertNotNull(loginTest, "login isNull");
        assertEquals(loginTest, "login");
        assertEquals(validateTest, true);
    }

    @Test
    void validateUserTestLoginIsEmpty(){
        User user = new User(12, "email@", "", "name", LocalDate.now());
        UserController controller = new UserController();

        try {
            controller.validate(user);
        }
        catch (ResponseStatusException exception) {
            assertEquals("400 BAD_REQUEST", exception.getMessage());
        }

        final String loginTest = user.getLogin();

        assertNotNull(loginTest, "login isNull");
        assertEquals(loginTest, "");
    }

    @Test
    void validateUserTestBirthday(){
        User user = new User(12, "email@", "login", "name", LocalDate.now());

        UserController controller = new UserController();
        final boolean validateTest = controller.validate(user);
        final LocalDate birthdayTest = user.getBirthday();

        assertNotNull(birthdayTest, "birthday isNull");
        assertEquals(birthdayTest, LocalDate.now());
        assertEquals(validateTest, true);
    }

    @Test
    void validateUserTestBirthdayIsEmpty(){
        User user = new User(12, "email@", "login", "name", LocalDate.now().plusDays(1));
        UserController controller = new UserController();

        try {
            controller.validate(user);
        }
        catch (ResponseStatusException exception) {
            assertEquals("400 BAD_REQUEST", exception.getMessage());
        }

        final LocalDate birthdayTest = user.getBirthday();

        assertNotNull(birthdayTest, "birthday isNull");
        assertEquals(birthdayTest, LocalDate.now().plusDays(1));
    }

    @Test
    void validateFilmTestName(){
        Film film = new Film(12, "film", "description", LocalDate.now().minusYears(10), 120);

        FilmController controller = new FilmController();
        final boolean validateTest = controller.validate(film);
        final String nameTest = film.getName();

        assertNotNull(nameTest, "name isNull");
        assertEquals(nameTest, "film");
        assertEquals(validateTest, true);
    }

    @Test
    void validateFilmTestNameIsEmpty(){
        Film film = new Film(12, "", "description", LocalDate.now().minusYears(10), 120);
        FilmController controller = new FilmController();

        try {
            controller.validate(film);
        }
        catch (ResponseStatusException exception) {
            assertEquals("400 BAD_REQUEST", exception.getMessage());
        }

        final String nameTest = film.getName();

        assertNotNull(nameTest, "name isNull");
        assertEquals(nameTest, "");
    }

    @Test
    void validateFilmTestDescription(){
        Film film = new Film(12, "film", "description", LocalDate.now().minusYears(10), 120);

        FilmController controller = new FilmController();
        final boolean validateTest = controller.validate(film);
        final String descriptionTest = film.getDescription();

        assertNotNull(descriptionTest, "description isNull");
        assertEquals(descriptionTest, "description");
        assertEquals(validateTest, true);
    }

    @Test
    void validateFilmTestDescriptionIsOver200(){
        String description = "description".repeat(20);
        Film film = new Film(12, "film", description, LocalDate.now().minusYears(10), 120);
        FilmController controller = new FilmController();

        try {
            controller.validate(film);
        }
        catch (ResponseStatusException exception) {
            assertEquals("400 BAD_REQUEST", exception.getMessage());
        }

        final String descriptionTest = film.getDescription();

        assertNotNull(descriptionTest, "description isNull");
    }

    @Test
    void validateFilmTestRelease(){
        Film film = new Film(12, "film", "description", LocalDate.now().minusYears(10), 120);

        FilmController controller = new FilmController();
        final boolean validateTest = controller.validate(film);
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
            controller.validate(film);
        }
        catch (ResponseStatusException exception) {
            assertEquals("400 BAD_REQUEST", exception.getMessage());
        }

        final LocalDate releaseTest = film.getReleaseDate();

        assertNotNull(releaseTest, "release isNull");
        assertEquals(releaseTest, LocalDate.now().minusYears(200));
    }

    @Test
    void validateFilmTestDuration(){
        Film film = new Film(12, "film", "description", LocalDate.now().minusYears(12), 120);

        FilmController controller = new FilmController();
        final boolean validateTest = controller.validate(film);
        final long durationTest = film.getDuration();

        assertNotNull(durationTest, "duration isNull");
        assertEquals(durationTest, 120);
        assertEquals(validateTest, true);
    }

    @Test
    void validateFilmTestDurationNegative(){
        Film film = new Film(12, "film", "description", LocalDate.now().minusYears(12), -120);
        FilmController controller = new FilmController();

        try {
            controller.validate(film);
        }
        catch (ResponseStatusException exception) {
            assertEquals("400 BAD_REQUEST", exception.getMessage());
        }

        final long durationTest = film.getDuration();

        assertNotNull(durationTest, "duration isNull");
        assertEquals(durationTest, -120);

    }
}

