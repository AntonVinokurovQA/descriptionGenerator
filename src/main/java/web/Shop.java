package web;

import data.PhoneInfo;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Shop {
    private final String BASE_URL = "https://stores-apple.com";
    private String modelsListUrl;

    public Shop(String modelsListUrl) {
        this.modelsListUrl = modelsListUrl;
    }

    public String getModelsListUrl() {
        return modelsListUrl;
    }

    public static void main(String[] args) {
        Shop shop = new Shop("https://stores-apple.com/catalog/smartfony/xiaomi/xiaomi_redmi_k/xiaomi_redmi_k70_pro/");

        String[] modelsUrl = shop.getModelsUrl();

        for (String url : modelsUrl) {
            System.out.println(url);
        }

        PhoneInfo[] phoneInfo = shop.getListOfDetails(modelsUrl);
        for (PhoneInfo info : phoneInfo) {
            System.out.println(info.getTitle());
            System.out.println(info.getCharacteristics());
        }
    }

    public String[] getModelsUrl() {
        String[] modelsUrl;

        Response response = RestAssured.get(modelsListUrl);
        String htmlBody = response.getBody().asString();
        Document document = Jsoup.parse(htmlBody);
        Elements elements = document.selectXpath("//div[contains(@class, 'catalog_block')]//a[@class='thumb']");

        modelsUrl = new String[elements.size()];

        for (int i = 0; i < elements.size(); i++) {
            modelsUrl[i] = BASE_URL + elements.get(i).attr("href");
        }

        return modelsUrl;
    }

    public PhoneInfo[] getListOfDetails(String[] modelsUrl) {
        PhoneInfo[] phoneInfo = new PhoneInfo[modelsUrl.length];

        for (int i = 0; i < modelsUrl.length; i++) {
            Response response = RestAssured.get(modelsUrl[i]);
            String htmlBody = response.getBody().asString();
            Document document = Jsoup.parse(htmlBody);

            String title = document.selectXpath("//h1[@id='pagetitle']").text();
            String characteristics = document.selectXpath("//div[@class = 'properties-group js-offers-group-wrap']").text();

            phoneInfo[i] = new PhoneInfo(title, characteristics);
        }

        return phoneInfo;
    }

}
