package webcrawlerir.processing;

import java.util.*;

public class QueryProcessor {
    private TFIDFCalculator tfidfCalculator;
    private TextProcessor textProcessor;

    public QueryProcessor(TFIDFCalculator tfidfCalculator) {
        this.tfidfCalculator = tfidfCalculator;
        this.textProcessor = new TextProcessor();
    }

    public void processQuery(List<String> tokens) {

        //Count TF in query
        Map<String, Integer> queryTermFreq = new HashMap<>();
        for (String token : tokens) {
            queryTermFreq.put(token, queryTermFreq.getOrDefault(token, 0) + 1);
        }

        //Calculate query TF-IDF vector
        Map<String, Double> queryTfIdf = new HashMap<>();
        double queryNorm = 0.0;
        for (Map.Entry<String, Integer> entry : queryTermFreq.entrySet()) {
            String term = entry.getKey();
            int tf = entry.getValue();
            double tfWeight = 1 + Math.log10(tf);

            Double idf = tfidfCalculator.Inverse_Document_Frequency.get(term);
            if (idf != null) {
                double tfidf = tfWeight * idf;
                queryTfIdf.put(term, tfidf);
                queryNorm += tfidf * tfidf;
            }
        }
        queryNorm = Math.sqrt(queryNorm);

        System.out.println("==== Query TF-IDF Vector ====");
        for (Map.Entry<String, Double> entry : queryTfIdf.entrySet()) {
            System.out.println("Term: " + entry.getKey() + ", TF-IDF: " + entry.getValue());
        }
        System.out.println("Query Norm: " + queryNorm);

        if (queryTfIdf.isEmpty()) {
            System.out.println("No matching terms found in documents for this query.");
            return;
        }

        //Cosine similarity
        Map<Integer, Double> docScores = new HashMap<>();
        for (Map.Entry<Integer, HashMap<String, Double>> docEntry : tfidfCalculator.tf_idfTable.entrySet()) {
            int docId = docEntry.getKey();
            Map<String, Double> docVector = docEntry.getValue();
            double dotProduct = 0.0;

            for (String term : queryTfIdf.keySet()) {
                if (docVector.containsKey(term)) {
                    dotProduct += queryTfIdf.get(term) * docVector.get(term);
                }
            }

            Double docNorm = tfidfCalculator.Norm.get(docId);
            if (docNorm != null && docNorm != 0 && queryNorm != 0) {
                double cosineSimilarity = dotProduct / (docNorm * queryNorm);
                docScores.put(docId, cosineSimilarity);
            }
        }

        if (docScores.isEmpty()) {
            System.out.println("No documents matched your query.");
            return;
        }

        //Output top 10
        System.out.println("\nTop 10 documents for your query:");
        docScores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .forEach(entry -> {
                    System.out.println("Document ID: " + entry.getKey() + " | Similarity Score: " + entry.getValue());
                });
    }
}