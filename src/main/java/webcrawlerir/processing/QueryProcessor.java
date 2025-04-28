package webcrawlerir.processing;

import java.util.*;

public class QueryProcessor {
    private TFIDFCalculator tfidfCalculator;
    private TextProcessor textProcessor;

    public QueryProcessor(TFIDFCalculator tfidfCalculator) {
        this.tfidfCalculator = tfidfCalculator;
        this.textProcessor = new TextProcessor();
    }

    public void processQuery(List<String> words) {
        // Count term frequency (TF) in the query
        Map<String, Integer> queryTermFreq = new HashMap<>();
        for (String token : words) {
            queryTermFreq.put(token, queryTermFreq.getOrDefault(token, 0) + 1);
        }

        // Calculate query's TF-IDF vector
        Map<String, Double> queryTfIdf = new HashMap<>();
        double queryNorm = 0.0;
        for (Map.Entry<String, Integer> entry : queryTermFreq.entrySet()) {
            String term = entry.getKey();
            int tf = entry.getValue();
            double tfWeight = 1 + Math.log10(tf);  // TF weight (log scale)

            // Get IDF for the term from the TF-IDF calculator
            Double idf = tfidfCalculator.Inverse_Document_Frequency.get(term);
            if (idf != null) {
                double tfidf = tfWeight * idf;  // TF-IDF calculation
                queryTfIdf.put(term, tfidf);
                queryNorm += tfidf * tfidf;  // Accumulate for norm calculation
            }
        }
        queryNorm = Math.sqrt(queryNorm);  // Normalize query vector

        // Calculate cosine similarity between query and document vectors
        Map<Integer, Double> docScores = new HashMap<>();
        for (Map.Entry<Integer, HashMap<String, Double>> docEntry : tfidfCalculator.tf_idfTable.entrySet()) {
            int docId = docEntry.getKey();
            Map<String, Double> docVector = docEntry.getValue();
            double dotProduct = 0.0;

            // Calculate dot product between query vector and document vector
            for (String term : queryTfIdf.keySet()) {
                if (docVector.containsKey(term)) {
                    dotProduct += queryTfIdf.get(term) * docVector.get(term);
                }
            }

            // Calculate cosine similarity (dot product / norms)
            Double docNorm = tfidfCalculator.Norm.get(docId);
            if (docNorm != null && docNorm != 0 && queryNorm != 0) {
                double cosineSimilarity = dotProduct / (docNorm * queryNorm);
                docScores.put(docId, cosineSimilarity);  // Store similarity score
            }
        }

        // No documents matched the query
        if (docScores.isEmpty()) {
            System.out.println("No documents matched your query.");
            return;
        }

        // Output top 10 documents based on similarity score
        System.out.println("\nTop 10 documents for your query:");
        docScores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))  // Sort by similarity score
                .limit(10)  // Limit to top 10 results
                .forEach(entry -> {
                    System.out.println("Document ID: " + entry.getKey() + " | Similarity Score: " + entry.getValue());
                });
    }
}
