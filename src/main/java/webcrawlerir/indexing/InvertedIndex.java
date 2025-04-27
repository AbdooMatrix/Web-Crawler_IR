package webcrawlerir.indexing;

import webcrawlerir.processing.TextProcessor;
import java.util.*;

public class InvertedIndex {
    private HashMap<String, List<Posting>> index;
    private TextProcessor textProcessor;
    private int totalDocs;

    public InvertedIndex() {
        index = new HashMap<>();
        textProcessor = new TextProcessor();
        totalDocs = 0;
    }

    public void buildIndex(Map<Integer, String> docIdToText) {
        totalDocs = docIdToText.size();

        for (Map.Entry<Integer, String> entry : docIdToText.entrySet()) {
            int docId = entry.getKey();
            String text = entry.getValue();

            List<String> tokens = textProcessor.processText(text);

            // count term frequencies for this document
            Map<String, Integer> termFrequencies = new HashMap<>();
            for (String token : tokens) {
                termFrequencies.put(token, termFrequencies.getOrDefault(token, 0) + 1);
            }

            for (Map.Entry<String, Integer> termEntry : termFrequencies.entrySet()) {
                String term = termEntry.getKey();
                int termFrequency = termEntry.getValue();

                // get or create the posting list for this term
                List<Posting> postings = index.getOrDefault(term, new ArrayList<>());
                postings.add(new Posting(docId, termFrequency));
                index.put(term, postings);
            }
        }
    }

    public HashMap<String, List<Posting>> getIndex() {
        return index;
    }
    //getter for total num of doc
    public int getTotalDocs() {
        return totalDocs;
    }

    //print the inverted index
    public void printIndex() {
        for (Map.Entry<String, List<Posting>> entry : index.entrySet()) {
            String term = entry.getKey();
            List<Posting> postings = entry.getValue();
            System.out.println("Term: " + term + " -> " + postings);
        }
        System.out.println("Total terms: " + index.size());
    }
}