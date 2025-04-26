package webcrawlerir;

import webcrawlerir.indexing.InvertedIndex;
import webcrawlerir.indexing.Posting;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Step 1: Run the web crawler
        WebCrawler crawler = new WebCrawler();
        Map<Integer, String> docIdToText = crawler.startCrawl();
        System.out.println("Crawled Pages: " + crawler.visitedUrls);

        // Step 2: Build the inverted index
        InvertedIndex invertedIndex = new InvertedIndex();
        invertedIndex.buildIndex(docIdToText);

        // Step 3: Print the inverted index
        invertedIndex.printIndex();

        // Step 4: Verify the inverted index
        String testTerm = "pharaoh"; // Example term to verify
        List<Posting> postings = invertedIndex.getIndex().get(testTerm);

        if (postings != null) {
            System.out.println("Term '" + testTerm + "' is mapped to the following documents:");
            for (Posting posting : postings) {
                System.out.println(posting);
            }
        } else {
            System.out.println("Term '" + testTerm + "' is not found in the inverted index.");
        }
    }
}