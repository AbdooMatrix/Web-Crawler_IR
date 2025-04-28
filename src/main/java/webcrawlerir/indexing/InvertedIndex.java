package webcrawlerir.indexing;

import webcrawlerir.processing.TextProcessor;
import java.util.*;

public class InvertedIndex {
    private HashMap<String, List<Posting>> index; // term -> list of postings
    private TextProcessor textProcessor; // handles text cleaning
    private int totalDocs; // number of documents

    public InvertedIndex() {
        index = new HashMap<>();
        textProcessor = new TextProcessor();
        totalDocs = 0;
    }

    // builds the inverted index from given documents
    public void buildIndex(Map<Integer, String> docIdToText) {
        totalDocs = docIdToText.size();

        for (Map.Entry<Integer, String> entry : docIdToText.entrySet()) {
            int docId = entry.getKey();
            String text = entry.getValue();

            List<String> tokens = textProcessor.processText(text);

            // count how many times each term appears in this document
            Map<String, Integer> termFrequencies = new HashMap<>();
            for (String token : tokens) {
                termFrequencies.put(token, termFrequencies.getOrDefault(token, 0) + 1);
            }

            for (Map.Entry<String, Integer> termEntry : termFrequencies.entrySet()) {
                String term = termEntry.getKey();
                int termFrequency = termEntry.getValue();

                // find or make posting list for this term
                List<Posting> postings = index.getOrDefault(term, new ArrayList<>());
                postings.add(new Posting(docId, termFrequency));
                index.put(term, postings);
            }
        }
    }

    // return the index
    public HashMap<String, List<Posting>> getIndex() {
        return index;
    }

    // return number of documents
    public int getTotalDocs() {
        return totalDocs;
    }

    // show the index contents
    public void printIndex() {
        for (Map.Entry<String, List<Posting>> entry : index.entrySet()) {
            String term = entry.getKey();
            List<Posting> postings = entry.getValue();
            System.out.println("Term: " + term + " -> " + postings);
        }
        System.out.println("Total terms: " + index.size());
    }
}
