import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;


public class CardDeliveryTest {

    @BeforeAll
    public static void setUpAll() {

        Configuration.browser = "firefox";
    }

    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void formCompleteSuccess() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Хабаровск");
        String currentDate = generateDate(4, "dd.MM.yyyy");
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys(currentDate);
        $("[data-test-id='name'] input").setValue("Воронина Елена");
        $("[data-test-id='phone'] input").setValue("+79109643232");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
    }

    @Test
    void dateEarlier3Days() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Хабаровск");
        String currentDate = generateDate(2, "dd.MM.yyyy");
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys(currentDate);
        $("[data-test-id='name'] input").setValue("Воронина Елена");
        $("[data-test-id='phone'] input").setValue("+79109643232");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='date'] .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void latinLettersInNameFailed() {

        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Хабаровск");
        String currentDate = generateDate(4, "dd.MM.yyyy");
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys(currentDate);
        $("[data-test-id='name'] input").setValue("Voronina Елена");
        $("[data-test-id='phone'] input").setValue("+79109643232");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='name'] .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void phoneNumberLengthFailed() {

        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Хабаровск");
        String currentDate = generateDate(4, "dd.MM.yyyy");
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys(currentDate);
        $("[data-test-id='name'] input").setValue("Воронина Елена");
        $("[data-test-id='phone'] input").setValue("79109643232");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void agreementUncheckedFailed() {

        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Хабаровск");
        String currentDate = generateDate(4, "dd.MM.yyyy");
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys(currentDate);
        $("[data-test-id='name'] input").setValue("Воронина Елена");
        $("[data-test-id='phone'] input").setValue("+79109643232");
        $("button.button").click();
        $("[data-test-id='agreement'] .checkbox__text")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void cityDateFromDroppedList() {

        open("http://localhost:9999");
        $("[data-test-id='city'] input").sendKeys("Ха");
        $$(".menu-item__control").findBy(text("Хабаровск")).click();
        $(".icon-button__text").click();
        $$("[data-day]").findBy(text("19")).click();
        $("[data-test-id='name'] input").setValue("Воронина Елена");
        $("[data-test-id='phone'] input").setValue("+79109643232");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
    }
}

