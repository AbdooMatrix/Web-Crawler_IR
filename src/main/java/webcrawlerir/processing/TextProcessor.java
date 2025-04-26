package webcrawlerir.processing;

import java.util.ArrayList;
import java.util.List;

public class TextProcessor {
    private static final List<String> STOP_WORDS = List.of(
            "the", "to", "be", "for", "from", "in", "a", "into", "by", "or", "and", "that"
    );

    public List<String> processText(String text) {
        List<String> tokens = new ArrayList<>();

        // tokenize using the regex
        String[] words = text.split("\\W+");

        // normalize
        for (String word : words) {

            word = word.toLowerCase();

            if (word.isEmpty()) {
                continue;
            }

            if (STOP_WORDS.contains(word) || word.length() < 2) {
                continue;
            }

            word = stemWord(word);

            tokens.add(word);
        }

        return tokens;
    }

    private String stemWord(String word) {
        Stemmer s = new Stemmer();
        s.addString(word);
        s.stem();
        return s.toString();
    }
}