package webcrawlerir.processing;

import java.util.ArrayList;
import java.util.List;

public class TextProcessor {
    // Common stop words to ignore
    private static final List<String> STOP_WORDS = List.of(
            "the", "to", "be", "for", "from", "in", "a", "into", "by", "or", "and", "that"
    );

    public List<String> processText(String text) {
        List<String> tokens = new ArrayList<>();

        // Tokenize input text using non-word characters
        String[] words = text.split("\\W+");

        // Normalize and clean words
        for (String word : words) {
            word = word.toLowerCase(); // Convert to lowercase

            if (word.isEmpty()) {
                continue; // Skip empty words
            }

            if (STOP_WORDS.contains(word) || word.length() < 2) {
                continue; // Skip stop words and very short words
            }

            word = stemWord(word); // Apply stemming

            tokens.add(word); // Add cleaned word to tokens
        }

        return tokens;
    }

    private String stemWord(String word) {
        // Apply stemming algorithm to the word
        Stemmer s = new Stemmer();
        s.addString(word);
        s.stem();
        return s.toString();
    }
}
