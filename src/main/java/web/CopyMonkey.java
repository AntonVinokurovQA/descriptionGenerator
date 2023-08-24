package web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

/**
 * Class for working with the CopyMonkey web application.
 */
public class CopyMonkey {
    private final String BASE_URL = "https://ai.copymonkey.app/";
    private String login;
    private String password;

    /**
     * Constructor of the class with Selenide settings.
     *
     * @param login    user's login.
     * @param password user's password.
     */
    public CopyMonkey(String login, String password) {
        Configuration.timeout = 90000;
        Configuration.browserSize = "1920x1080";
        Configuration.headless = false;

        this.login = login;
        this.password = password;
    }

    /**
     * Empty constructor of the class.
     */
    public CopyMonkey() {
        this.login = "";
        this.password = "";
    }

    /**
     * Method for logging into the system.
     *
     * @return true if login is successful, otherwise - false.
     */
    public boolean login() {
        open(BASE_URL);

        $x("//button[text() = 'Войти' or text() = 'Login'] ").click();
        $x("//input[@name = 'email']").setValue(login);
        $x("//input[@name = 'password']").setValue(password);
        $x("//button[text() = 'Войти' or text() = 'Login'] ").click();

        $x("//h3[text() = 'Product Description' or text() = 'Описание товара']").shouldBe(visible);

        return true;
    }

    /**
     * Method for navigating to the product description page.
     *
     * @return the current instance of the CopyMonkey class.
     */
    public CopyMonkey goToProductDescription() {
        open(BASE_URL);

        $x("//h3[text() = 'Product Description'  or text() = 'Описание товара']").click();
        $x("//button[text() = 'Generate' or text() = 'Сгенерировать']").shouldBe(visible);

        return this;
    }

    /**
     * Method for generating a product description.
     *
     * @param title            title of the product description.
     * @param productProperties properties of the product.
     * @return generated product description.
     */
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

        Selenide.sleep(10000);

        // Entering product properties
        $x("//textarea[@id = ':r1:']").clear();
        $x("//textarea[@id = ':r1:']").sendKeys(productProperties);

        $x("//button[text() = 'Generate' or text() = 'Сгенерировать']").click();

        // Waiting for the description to generate
        $x("//h5[@class = 'MuiTypography-root MuiTypography-h5 css-rsuf5k']").shouldBe(visible);

        // Appending the generated description to the result
        description += $x("//h5[@class = 'MuiTypography-root MuiTypography-h5 css-rsuf5k']").getOwnText();
        Selenide.refresh();
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();

        return description;
    }
}
