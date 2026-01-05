package tests;

import data.Subjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import pages.RegistrationPage;

import java.util.List;
import java.util.stream.Stream;

public class RegistrationWithPageObjectsTests extends tests.TestBase {

    RegistrationPage registrationPage = new RegistrationPage();

    @CsvFileSource(resources = "/validateCityCountry.csv")
    @ParameterizedTest(name = "Проверка соответствия выбранной страны {0} городу {1}")
    void validateCityState(String state, String city) {
        registrationPage.openPage()
                .removeBanners()
                .setFirstName("Svetlana")
                .setLastName("Abramova")
                .setEmail("sabramova@abrams.com")
                .setGender("Female")
                .setUserNumber("1234567890")
                .setDateOfBirth("10", "January", "1994")
                .setSubjects("Physics")
                .setHobbies("Sports")
                .setUploadPicture("check.PNG")
                .setCurrentAddress("Pushkin Street 1")
                .setStateDropdown()
                .setStateCityWrapper(state)
                .setCityDropdown()
                .setStateCityWrapper(city)
                .setSubmit()
                .setModalDialog()
                .checkResult("Student Name", "Svetlana Abramova")
                .checkResult("Student Email", "sabramova@abrams.com")
                .checkResult("Gender", "Female")
                .checkResult("Mobile", "1234567890")
                .checkResult("Date of Birth", "10 January,1994")
                .checkResult("Subjects", "Physics")
                .checkResult("Hobbies", "Sports")
                .checkResult("Picture", "check.PNG")
                .checkResult("Address", "Pushkin Street 1")
                .checkResult("State and City", state + " " + city)
                .checkResult("Picture", "check.PNG")
                .closeModal();
    }

    @ValueSource(strings = {
            "Svetlana",
            "Светлана",
            "Anna-Maria"
    })
    @ParameterizedTest(name = "Успешная регистрация при вводе {0} в поле 'FirstName'")
    @Tag("SMOKE")
    void checkFirstNameValidation(String firstName) {
        registrationPage.openPage()
                .removeBanners()
                .setFirstName(firstName)
                .setLastName("Abramova")
                .setGender("Female")
                .setUserNumber("1234567890")
                // отображается текущая дата по дефолту
                .setDateOfBirth("10", "January", "1994")
                .setSubmit()
                .setModalDialog()
                .checkResult("Student Name", firstName + " Abramova")
                .checkResult("Gender", "Female")
                .checkResult("Mobile", "1234567890")
                .checkResult("Date of Birth", "10 January,1994")
                .closeModal();
    }

    @EnumSource(Subjects.class)
    @ParameterizedTest(name = "Успешная регистрация с заполнением обязательных полей и поля Subjects значением {0}")
    void successfulRegistrationWithSubjects(Subjects subjects) {
        registrationPage.openPage()
                .removeBanners()
                .setFirstName("Svetlana")
                .setLastName("Abramova")
                .setEmail("sabramova@abrams.com")
                .setGender("Female")
                .setUserNumber("1234567890")
                .setDateOfBirth("10", "January", "1994")
                .setSubjects(subjects.name())
                .setSubmit()
                .setModalDialog()
                .checkResult("Student Name", "Svetlana Abramova")
                .checkResult("Student Email", "sabramova@abrams.com")
                .checkResult("Gender", "Female")
                .checkResult("Mobile", "1234567890")
                .checkResult("Date of Birth", "10 January,1994")
                .checkResult("Subjects", subjects.name())
                .closeModal();
    }

    static Stream<Arguments> validateFirstNameLastNameSubjects() {
        return Stream.of(
                Arguments.of(
                        "Svetlana", "Abramova",
                        String.valueOf(Subjects.Arts)
                ),
                Arguments.of(
                        "Egor", "Antonov",
                        String.valueOf(Subjects.Biology)
                )
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Проверка соответствия имени {0} и фамилии {1} специальности {2}")
    void validateFirstNameLastNameSubjects(String firstname, String lastname, String subjects) {
        registrationPage.openPage()
                .removeBanners()
                .setFirstName(firstname)
                .setLastName(lastname)
                .setEmail("sabramova@abrams.com")
                .setGender("Female")
                .setUserNumber("1234567890")
                .setDateOfBirth("10", "January", "1994")
                .setSubjects(subjects)
                .setSubmit()
                .setModalDialog()
                .checkResult("Student Name", firstname + " " + lastname)
                .checkResult("Student Email", "sabramova@abrams.com")
                .checkResult("Gender", "Female")
                .checkResult("Mobile", "1234567890")
                .checkResult("Date of Birth", "10 January,1994")
                .checkResult("Subjects", subjects)
                .closeModal();
    }
}