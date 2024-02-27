package web;

import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class CopyMonkey {
    private final String BASE_URL = "https://ai.copymonkey.app/";
    private String login;
    private String password;

    public static void main(String[] args) {
        CopyMonkey copyMonkey = new CopyMonkey("i.antipof@gmail.com", "(r(lSc@iWoCf(b(@Y9TG");

        copyMonkey.login();
    }

    public CopyMonkey(String login, String password) {
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


        Selenide.sleep(50000);


        return true;
    }

}
