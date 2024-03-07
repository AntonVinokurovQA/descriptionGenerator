package web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class CopyMonkey {
    private final String BASE_URL = "https://ai.copymonkey.app/";
    private String login;
    private String password;

    public CopyMonkey(String login, String password) {
        Configuration.timeout = 60000;
        Configuration.browserSize = "1920x1080";
        Configuration.headless = true;

        this.login = login;
        this.password = password;
    }

    public CopyMonkey() {
        this.login = "";
        this.password = "";
    }

    public boolean login() {
        open(BASE_URL);

        $x("//button[text() = 'Войти']").click();
        $x("//input[@name = 'email']").setValue(login);
        $x("//input[@name = 'password']").setValue(password);
        $x("//button[text() = 'Войти']").click();

        $x("//h3[text() = 'Описание товара']").shouldBe(visible);

        return true;
    }

    public CopyMonkey goToProductDescription() {
        open(BASE_URL);

        $x("//h3[text() = 'Описание товара']").click();
        $x("//button[text() = 'Сгенерировать']").shouldBe(visible);

        return this;
    }

    public String generateDescription(String title, String productProperties) {
        String description = "";

        if (productProperties.length() > 2000) {
            String part1 = productProperties.substring(0, 2000);
            productProperties = productProperties.substring(2000);
            description = generateDescription(title, part1);
        }

        $x("//input[@id = ':r0:']").shouldBe(visible);
        $x("//input[@id = ':r0:']").clear();
        $x("//input[@id = ':r0:']").sendKeys(title);

        $x("//textarea[@id = ':r1:']").clear();
        $x("//textarea[@id = ':r1:']").sendKeys(productProperties);

        Selenide.sleep(10000);

        $x("//button[text() = 'Сгенерировать']").click();

        $x("//h5[@class = 'MuiTypography-root MuiTypography-h5 css-rsuf5k']").shouldBe(visible);


        description +=   $x("//h5[@class = 'MuiTypography-root MuiTypography-h5 css-rsuf5k']").getOwnText();
        Selenide.refresh();

        return description;
    }
}
