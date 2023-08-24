package generator;

public class TextSplitter {
    public static String[] splitText(String text) {
        String[] parts = new String[4];
        int partLength = (int) Math.ceil(text.length() / 4.0);

        int currentIndex = 0;
        for (int i = 0; i < 3; i++) {
            int endIndex = findSplitIndex(text, currentIndex + partLength);
            parts[i] = text.substring(currentIndex, endIndex).trim();
            currentIndex = endIndex;
        }

        parts[3] = text.substring(currentIndex).trim();
        return parts;
    }

    private static int findSplitIndex(String text, int endIndex) {
        if (endIndex >= text.length()) {
            return text.length();
        }

        int lastDotIndex = text.lastIndexOf('.', endIndex);
        if (lastDotIndex == -1) {
            return endIndex;
        } else {
            return lastDotIndex + 1;
        }
    }

    public static void main(String[] args) {
        String exampleText = "This is an example text. It needs to be split into four parts while maintaining sentence integrity. The split should happen without breaking sentences. The final part should contain the remaining text.";
        String[] parts = splitText(exampleText);

        for (String part : parts) {
            System.out.println(part);
            System.out.println("-------------------");
        }
    }
}
