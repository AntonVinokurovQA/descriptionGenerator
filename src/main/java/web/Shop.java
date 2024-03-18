package web;

import data.PhoneInfo;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Class for interacting with an online shop.
 */
public class Shop {
    private final String BASE_URL = "https://stores-apple.com";
    private String modelsListUrl;

    /**
     * Constructor for the Shop class.
     *
     * @param modelsListUrl URL of the models list.
     */

    public Shop(String modelsListUrl) {
        this.modelsListUrl = modelsListUrl;
    }

    /**
     * Method to get the models list URL.
     *
     * @return models list URL.
     */
    public String getModelsListUrl() {
        return modelsListUrl;
    }

    /**
     * Method to retrieve URLs of individual models.
     *
     * @return array of model URLs.
     */
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

    /**
     * Method to retrieve details of each phone model.
     *
     * @param modelsUrl array of model URLs.
     * @return array of PhoneInfo objects containing model details.
     */
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
