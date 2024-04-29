package generator;

/**
 * A class representing a tool for splitting text into parts.
 * This class contains methods for splitting text into a specified number of parts.
 */
public class TextSplitter {
    /**
     * A method for splitting text into parts.
     *
     * @param text The original text to be split.
     * @return An array of strings containing the parts of the text.
     */
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

    /**
     * A helper method to find the index of splitting the text.
     *
     * @param text     The original text.
     * @param endIndex The presumed index of the end of the text part.
     * @return The index of splitting the text.
     */
    private static int findSplitIndex(String text, int endIndex) {
        if (endIndex >= text.length()) {
            return text.length();
        }

        int lastDotIndex = text.lastIndexOf(". ", endIndex);
        if (lastDotIndex == -1) {
            return endIndex;
        } else {
            return lastDotIndex + 1;
        }
    }
}
