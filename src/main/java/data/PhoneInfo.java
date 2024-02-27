package data;

public class PhoneInfo {
    private String title;
    private String characteristics;

    public PhoneInfo(String title, String characteristics) {
        this.title = title;
        this.characteristics = characteristics;
    }

    public PhoneInfo() {
        this.title = "";
        this.characteristics = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }
}
