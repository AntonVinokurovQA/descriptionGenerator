import data.PhoneInfo;
import web.CopyMonkey;
import web.Shop;

public class MainApp {
    static String ANSI_GREEN = "\u001B[32m";
    static String ANSI_RED = "\u001B[31m";
    static String ANSI_ORANGE = "\u001B[33m";
    static String ANSI_RESET = "\u001B[0m";
/**
 * This main entrance to the description generator
 * @args should contain 4 parameters
 * TARGET_URL - url of model page
 * USERNAME - login of copymonkey
 * PASSWORD - password of copymonkey
 * IMAGE_URL - url of pictures without index
 */
public static void main(String[] args) {
/*
        if (args.length != 1) {
            System.out.println(ANSI_RED + "Number of arguments should be only one..." + ANSI_RESET);
            System.out.println(ANSI_RED + "Exit.." + ANSI_RESET);
            System.exit(1);
        }
*/
        Shop shop = new Shop(args[0]);
        CopyMonkey copyMonkey = new CopyMonkey(args[1], args[2]);

        String[] modelsUrl = shop.getModelsUrl();

        System.out.println(ANSI_GREEN + "=================================================================================" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "Model's URL: " + shop.getModelsListUrl() + ANSI_RESET);
        System.out.println(ANSI_GREEN + "=================================================================================" + ANSI_RESET);
        System.out.println(ANSI_GREEN + modelsUrl.length + " phones have been found" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "Phone's URLs:" + ANSI_RESET);

        for (String url : modelsUrl) {
            System.out.println(url);
        }

        System.out.println(ANSI_GREEN + "=================================================================================" + ANSI_RESET);

        PhoneInfo[] phoneInfo = shop.getListOfDetails(modelsUrl);

        System.out.println(ANSI_GREEN + "Phones info:" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "=================================================================================" + ANSI_RESET);


        for (PhoneInfo info : phoneInfo) {
            System.out.println(ANSI_ORANGE + "title: " + ANSI_RESET + info.getTitle());
            System.out.println(ANSI_ORANGE + "characteristics: " + ANSI_RESET + info.getCharacteristics());
            System.out.println(" ");
        }

        System.out.println(ANSI_GREEN + "=================================================================================" + ANSI_RESET);


        String[] descriptions = new String[phoneInfo.length];

        System.out.println(ANSI_GREEN + "Running copymonkey ... " + ANSI_RESET);

        copyMonkey.login();
        copyMonkey.goToProductDescription();

        for (int i = 0; i < phoneInfo.length; i++) {
            descriptions[i] = copyMonkey.generateDescription(phoneInfo[i].getTitle(), phoneInfo[i].getCharacteristics());
            System.out.println("");
            System.out.println(phoneInfo[i].getTitle());
            System.out.println(descriptions[i]);
        }

        System.out.println(ANSI_GREEN + "\n=================================================================================" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "DONE ! It were generated " + descriptions.length + " descriptions." + ANSI_RESET);
        System.out.println(ANSI_GREEN + "=================================================================================" + ANSI_RESET);


    }
}
