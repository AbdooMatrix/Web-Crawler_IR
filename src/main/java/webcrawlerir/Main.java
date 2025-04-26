package webcrawlerir;

import webcrawlerir.indexing.InvertedIndex;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        Map<Integer, String> docIdToText = crawler.startCrawl();
        System.out.println("Crawled Pages: " + crawler.visitedUrls);

        // Test the TextProcessor with a sample document
//        TextProcessor processor = new TextProcessor();
//        String sampleText = docIdToText.get(0); // Get the text of the first document
//        if (sampleText != null) {
//            // Process only the first 100 characters for testing
//            String testText = sampleText.length() > 100 ? sampleText.substring(0, 100) : sampleText;
//            System.out.println("Sample Text: " + testText);
//
//            List<String> tokens = processor.processText(testText);
//            System.out.println("Processed Tokens: " + tokens);
//        }

        // Build the InvertedIndex
        InvertedIndex invertedIndex = new InvertedIndex();
        invertedIndex.buildIndex(docIdToText);

        // Print the index
        invertedIndex.printIndex();
    }
}